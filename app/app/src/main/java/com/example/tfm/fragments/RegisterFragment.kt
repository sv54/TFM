package com.example.tfm.fragments

import ApiService
import CountryAdapter
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.tfm.ApiListener
import com.example.tfm.R
import com.example.tfm.activities.MainActivity
import com.example.tfm.models.UserLoginData
import com.example.tfm.models.UserRegisterData
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.mindrot.jbcrypt.BCrypt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import com.example.tfm.data.countryMap
import com.example.tfm.data.flagAssets


class RegisterFragment : Fragment(), ApiListener {
    private val PICK_IMAGE_REQUEST = 1
    private val REQUEST_PERMISSION_CODE = 1001



    private lateinit var backButton: ImageButton
    private lateinit var registerButton: Button
    private lateinit var addPhotoButton: ImageView

    private lateinit var userRegisterData: UserRegisterData
    private lateinit var usernameEdit:EditText
    private lateinit var emailEdit:EditText
    private lateinit var passwordEdit:EditText
    private lateinit var passwordRepeatEdit:EditText
    private lateinit var paisEdit:EditText
    private lateinit var errorTextView:TextView

    private var selectedImageUri: Uri? = null
    private var userId = -1
    private var finalUsername = ""
    private var finalEmail = ""
    private var finalMetaViajes = 0
    private var finalTokenSesion: String? = null
    private var finalExpSesion: String? = null
    private var finalFotoPerfil = ""
    private var finalPais = -1
    private var finalPaisId = 0
    private var finalPaisNombre = ""
    private var finalPaisISO = ""
    private var finalContinenteNombre = ""
    private var finalContinenteId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_register, container, false)

        usernameEdit = rootView.findViewById<EditText>(R.id.editUsername)
        emailEdit = rootView.findViewById<EditText>(R.id.editEmail)
        passwordEdit = rootView.findViewById<EditText>(R.id.editPassword)
        paisEdit = rootView.findViewById<AutoCompleteTextView>(R.id.editCountry)
        passwordRepeatEdit = rootView.findViewById<EditText>(R.id.editRepeatPassword)
        errorTextView = rootView.findViewById(R.id.textError)

        super.onCreate(savedInstanceState)

        val countries = countryMap.keys.toList()
        val adapter = CountryAdapter(requireContext(), countries, flagAssets)
        (paisEdit as AutoCompleteTextView?)?.setAdapter(adapter)

        (paisEdit as AutoCompleteTextView?)?.setOnItemClickListener { parent, _, position, _ ->
            val selectedCountry = parent.getItemAtPosition(position) as String
            val countryId = countryMap[selectedCountry]
            Log.i("tagg", "Country id: $countryId")
            if (countryId != null) {
                finalPais = countryId
            }
        }


        usernameEdit.setText("username")
        emailEdit.setText("email@gmail.com")
        passwordEdit.setText("123456")
        passwordRepeatEdit.setText("123456")
        //TODO delete this part



        addPhotoButton = rootView.findViewById(R.id.buttonAddPhoto)
        addPhotoButton.setOnClickListener{
            if (checkPermission()) {
                pickImageFromGallery()
            } else {
                requestPermission()
            }
        }

        backButton = rootView.findViewById(R.id.buttonBackToLogin)

        backButton.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

        registerButton = rootView.findViewById(R.id.buttonRegister)
        registerButton.setOnClickListener {

            val username = usernameEdit.text.toString()
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()

            if(finalPais < 1){
                setErrorMessage(getString(R.string.register_choose_at_least_one_country))
            }
            else {

                userRegisterData =
                    UserRegisterData(username, email, password, finalPais.toString(), "")
                val dataChecked = checkData(userRegisterData)

                if (dataChecked == "ok") {
                    hideError()
//                val salt = generateSalt()
//                val hashedPassword = hashPassword(userRegisterData.password, salt)
//                userRegisterData.password = hashedPassword
//                userRegisterData.salt = salt
                    register(userRegisterData, selectedImageUri)
                } else {
                    setErrorMessage(dataChecked)
                    Log.e("tagg", dataChecked)
                }
            }
        }



        return rootView

    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            selectedImageUri = data?.data
            // Cargar la imagen con BitmapFactory
            selectedImageUri?.let { uri ->
                try {
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    addPhotoButton.setImageBitmap(bitmap)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImage.launch(intent)
    }

    private fun register(data: UserRegisterData, selectedImageUri: Uri?) {
        // Crear las partes de texto para los campos de datos
        val nombreBody = data.username.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailBody = data.email.toRequestBody("text/plain".toMediaTypeOrNull())
        val passwordBody = data.password.toRequestBody("text/plain".toMediaTypeOrNull())
        val paisOrigenBody = data.paisOrigen.toRequestBody("text/plain".toMediaTypeOrNull())
        val saltBody = data.salt.toRequestBody("text/plain".toMediaTypeOrNull())

        // Preparar la parte de imagen si hay una URI seleccionada
        var photoPart: MultipartBody.Part? = null
        selectedImageUri?.let { uri ->
            val file = File(getRealPathFromUri(uri))
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            photoPart = MultipartBody.Part.createFormData("photo", file.name, requestFile)
        }

        // Crear la instancia del servicio Retrofit
        val apiService = RetrofitClient.instance.create(ApiService::class.java)

        // Hacer la llamada al servicio de registro
        val call = if (photoPart != null) {
            apiService.register(nombreBody, emailBody, passwordBody, paisOrigenBody, saltBody, photoPart)
        } else {
            apiService.register(nombreBody, emailBody, passwordBody, paisOrigenBody, saltBody)
        }

        // Ejecutar la llamada asíncrona
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    Log.i("tagg", userResponse.toString())
                    userResponse?.let { jsonObject ->
                        userId = jsonObject.get("id").asInt
                        finalUsername = jsonObject.get("nombre").asString
                        finalEmail = jsonObject.get("email").asString
                        finalFotoPerfil = jsonObject.get("fotoPerfil").asString
                        finalPaisId = jsonObject.get("paisId").asInt
                        finalPaisNombre = jsonObject.get("paisNombre").asString
                        finalPaisISO = jsonObject.get("paisIso").asString
                        finalContinenteNombre = jsonObject.get("continenteNombre").asString
                        finalContinenteId = jsonObject.get("continenteId").asInt
//                        finalTokenSesion = jsonObject.get("tokenSesion")?.asString
//                        finalExpSesion = jsonObject.get("expSesion").asString
                        finalMetaViajes = jsonObject.get("metaViajes").asInt
                        onEventCompleted()
                    }
                } else {
                    Log.i("tagg", "Error en la respuesta del servidor")
                    onEventFailed()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.i("tagg", "Error en la llamada al servidor: ${t.message}")
            }
        })
    }

    override fun onEventCompleted() {
        val sharedPreferences = requireContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("UserId", userId)
        editor.putString("UserUsername", finalUsername)
        editor.putString("UserEmail", finalEmail)
        editor.putString("UserPhoto", finalFotoPerfil)
        editor.putInt("UserPaisId",finalPaisId)
        editor.putString("UserPaisNombre",finalPaisNombre)
        editor.putString("UserPaisISO",finalPaisISO)
        editor.putString("UserContinenteNombre",finalContinenteNombre)
        editor.putInt("UserContinenteId",finalContinenteId)
        editor.putInt("UserMetaViajes", finalMetaViajes)
//        editor.putString("UserTokenSesion", finalTokenSesion)
//        editor.putString("UserExpSesion", finalExpSesion)
        editor.putInt("UserMetaViajes", finalMetaViajes)
        editor.apply()

        Log.i("tagg", "userId: " + sharedPreferences.getInt("UserId", -1).toString())

        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }


    override fun onEventFailed() {
        setErrorMessage(getString(R.string.email_already_in_use))
    }


    private fun generateSalt(): String {
        return BCrypt.gensalt()
    }

    private fun hashPassword(password: String, salt: String): String {
        return BCrypt.hashpw(password, salt)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()
        return emailPattern.matches(email)
    }


    private fun setErrorMessage(message: String) {
            errorTextView.text = message
            errorTextView.setTextColor(Color.RED)

    }
    private fun hideError() {
        errorTextView.text = ""
    }

    private fun checkData(data: UserRegisterData): String {
        if (!isValidEmail(data.email)) {
            return getString(R.string.email_invalid_error)
        }
        if (data.username.length < 6) {
            return getString(R.string.username_length_register_error)
        }
        if (data.username.all { it.isDigit() }) {
            return getString(R.string.username_all_number_register_error)
        }
        if (data.password != passwordRepeatEdit.text.toString()) {
            return getString(R.string.register_error_password_doesnt_match)
        }
        if (data.password.length < 6) {
            return getString(R.string.register_password_length_error)
        }
        return "ok"
    }

    private fun getRealPathFromUri(uri: Uri): String {
        val cursor = context?.contentResolver?.query(uri, null, null, null, null)
        cursor?.let {
            it.moveToFirst()
            val index = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            val path = it.getString(index)
            cursor.close()
            return path
        }
        return uri.path ?: ""
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
            REQUEST_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery()
            } else {
                Toast.makeText(requireContext(), getString(R.string.permission_not_granted), Toast.LENGTH_LONG).show()
            }
        }
    }

}