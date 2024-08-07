package com.example.tfm.fragments

import ApiService
import SharedPreferencesManager
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.tfm.ApiListener
import com.example.tfm.R
import com.example.tfm.activities.CommentActivity
import com.example.tfm.activities.FavoritesActivity
import com.example.tfm.activities.VisistedActivity
import com.example.tfm.models.UserUpdateMeta
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class ProfileFragment : Fragment(), ApiListener {

    private val REQUEST_PERMISSION_CODE = 1002

    private lateinit var imageProfile: ImageView
    private lateinit var usernameText: TextView
    private lateinit var emailText: TextView
    private lateinit var favoritoButton: Button
    private lateinit var visitadosButton: Button
    private lateinit var metaViajesText: TextView
    private lateinit var visitadosText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var flagImg: ImageView
    private lateinit var countryText: TextView

    private lateinit var sharedPreferences: SharedPreferences

    private var visitados: JsonArray = JsonArray()
    private var favoritos: JsonArray = JsonArray()
    private var historial: JsonArray = JsonArray()

    private var userId: Int = -1

    private var selectedImageUri: Uri? = null


    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            selectedImageUri = data?.data
            // Cargar la imagen con BitmapFactory
            selectedImageUri?.let { uri ->
                try {
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)

                    // Ajustar la orientación de la imagen
                    val rotatedBitmap = rotateBitmapIfRequired(bitmap, uri)

                    imageProfile.setImageBitmap(rotatedBitmap)
                    putUserProfileImage()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        } else {
            Log.i("tagg", "Profile new image failure")
        }
    }

    private fun rotateBitmapIfRequired(bitmap: Bitmap, uri: Uri): Bitmap {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val exif = ExifInterface(inputStream!!)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
            else -> bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply {
            postRotate(degrees)
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        sharedPreferences = requireContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        imageProfile = rootView.findViewById(R.id.imageProfilePhoto)
        usernameText = rootView.findViewById(R.id.textProfileUsername)
        emailText = rootView.findViewById(R.id.textProfileEmail)
        favoritoButton = rootView.findViewById(R.id.buttonProfileFavoritos)
        visitadosButton = rootView.findViewById(R.id.buttonProfileVisitados)
        metaViajesText = rootView.findViewById(R.id.textProfileMeta)
        visitadosText = rootView.findViewById(R.id.textProfileVisitadosCount)
        progressBar = rootView.findViewById(R.id.progressBarViajes)
        countryText = rootView.findViewById(R.id.textPaisProfile)
        flagImg = rootView.findViewById(R.id.imageFlagProfile)

        metaViajesText.setOnClickListener{
            showNumberPickerDialog()
        }
        visitadosText.setOnClickListener{
            showNumberPickerDialog()
        }
        progressBar.setOnClickListener{
            showNumberPickerDialog()
        }

        val dataUsername = sharedPreferences.getString("UserUsername", "")
        val dataEmail = sharedPreferences.getString("UserEmail", "")
        val dataProfileImage = sharedPreferences.getString("UserPhoto", "")
        userId = sharedPreferences.getInt("UserId", -1)
        if (userId == -1){
            Log.i("tagg", "OnBackPressed Profile")
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        countryText.text = sharedPreferences.getString("UserPaisNombre", "No especificado")
        var flagFilename = sharedPreferences.getString("UserPaisISO", "null")
        try {
            val assetManager = requireContext().assets
            val inputStream = assetManager.open("flags/${flagFilename?.lowercase()}.png")
            val bitmap = BitmapFactory.decodeStream(inputStream)
            flagImg.setImageBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("tagg", e.message.toString())
            // Manejo de error, por ejemplo, mostrar una imagen por defecto
            flagImg.setImageResource(R.drawable.ic_map) // imagen por defecto
        }

        //Log.i("tagg", "Favorite" + SharedPreferencesManager.getFavorites(requireContext()).toString())
        //Log.i("tagg", "Visited" + SharedPreferencesManager.getVisited(requireContext()).toString())
        //Log.i("tagg", "History" + SharedPreferencesManager.getHistory(requireContext()).toString())


        usernameText.text = dataUsername
        emailText.text = dataEmail

        imageProfile.setOnClickListener {
            val options = arrayOf("Borrar foto", "Elegir nueva foto")

            AlertDialog.Builder(requireContext())
                .setTitle("Opciones de Foto")
                .setItems(options) { dialog, which ->
                    when (which) {
                        0 -> {
                            selectedImageUri = null
                            deleteUserProfileImage()
                        }
                        1 -> {
                            if (checkPermission()) {
                                pickImageFromGallery()
                            } else {
                                requestPermission()
                            }
                        }
                    }
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()

        }



        if(dataProfileImage == "" || dataProfileImage!!.contains("sinFoto")){
            imageProfile.setImageResource(R.drawable.ic_empty_photo)
        }
        else{
            Glide.with(imageProfile.context)
                .load(dataProfileImage)
                .into(imageProfile)
        }


        favoritoButton.setOnClickListener {
            val intent = Intent(activity, FavoritesActivity::class.java)
            startActivity(intent)
        }

        visitadosButton.setOnClickListener {
            val intent = Intent(activity, VisistedActivity::class.java)
            startActivity(intent)
        }

        return rootView
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImage.launch(intent)
    }


    override fun onResume() {
        super.onResume()
        updateProgressBar()
    }

    private fun updateProgressBar(){
        val visitedList = SharedPreferencesManager.getVisited(requireContext())
        val visitedThisYear = SharedPreferencesManager.countVisitedInCurrentYear(visitedList)
        val metaViajes = sharedPreferences.getInt("UserMetaViajes", 0)
        if (metaViajes > 0) {
            val progressPercentage = (visitedThisYear * 100) / metaViajes
            progressBar.max = 100 // La barra de progreso se establece en un rango de 0 a 100 para el porcentaje
            progressBar.progress = progressPercentage
        } else if (metaViajes == 0 && visitedThisYear > 0){
            progressBar.max = 100
            progressBar.progress = 100
        }
        else{
            progressBar.max = 100
            progressBar.progress = 0
        }
        visitadosText.text = getString(R.string.visited_objective_profile) + visitedThisYear
        metaViajesText.text =  getString(R.string.goal_per_year_profile) + metaViajes
    }

    private fun showNumberPickerDialog(){
        val inflater = LayoutInflater.from(requireContext())
        val numberPickerView = inflater.inflate(R.layout.dialog_number_picker, null)

        val numberPicker = numberPickerView.findViewById<NumberPicker>(R.id.numberPicker)
        numberPicker.maxValue = 100
        numberPicker.minValue = 0

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.select_new_goal_profile)
            .setView(numberPickerView)
            .setPositiveButton("OK") { dialog, which ->
                val selectedNumber = numberPicker.value
                updateMetaViajes(selectedNumber)
//                Log.i("tagg", "selected number: " + selectedNumber)
            }
            .setNegativeButton("Cancel", null)
            .create()

        // Show the dialog
        dialog.show()
    }

    private fun updateMetaViajes(newMeta: Int){
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val userId = sharedPreferences.getInt("UserId", -1)
        val call = apiService.updateMetaViajes(UserUpdateMeta(userId, newMeta))
        if (userId == -1){
            return
        }

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val editor = sharedPreferences.edit()
                    editor.putInt("UserMetaViajes", newMeta)
                    editor.apply()
                    updateProgressBar()
                } else {
                    Log.e("tagg", response.body().toString())
                    Toast.makeText(requireContext(), "Something went wrong, try later", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(requireContext(), "Something went wrong, try later", Toast.LENGTH_SHORT).show()
                Log.i("tagg", "en el failure pues " + t.message)
            }
        })
    }

    private fun getUserData(){
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val userId = sharedPreferences.getInt("UserId", -1)
        val call = apiService.getUserData(userId)
        if (userId == -1){
            return
        }

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    userResponse?.let { jsonObject ->
                        favoritos = jsonObject.getAsJsonArray("favoritos")
                        visitados = jsonObject.getAsJsonArray("visitados")
                        historial = jsonObject.getAsJsonArray("historial")
                        Log.i("tagg", favoritos.toString())
                        onEventCompleted()
                    }

                } else {
                    Log.i("tagg", "en el else pues")
                    onEventFailed()

                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.i("tagg", "en el failure pues " + t.message)
                onEventFailed()
            }
        })
    }

    private fun deleteUserProfileImage(){
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.deleteUserPhoto(userId)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    userResponse?.let { jsonObject ->
                        val editor = sharedPreferences.edit()
                        val newPhoto = jsonObject.get("newPhoto").asString
                        editor.putString("UserPhoto", newPhoto)
                        editor.apply()
                    }
                    Toast.makeText(requireContext(), getString(R.string.profile_picture_uploaded_success), Toast.LENGTH_LONG).show()
                    imageProfile.setImageResource(R.drawable.ic_empty_photo)

                } else {
                    Toast.makeText(requireContext(), getString(R.string.profile_picture_error), Toast.LENGTH_LONG).show()
                    Log.i("tagg","Error al actualizar la foto de perfil: ${response.errorBody()?.string()}")
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.i("tagg","Error en la solicitud: ${t.message}")
            }
        })
    }


    private fun putUserProfileImage(){
        var photoPart: MultipartBody.Part? = null
        selectedImageUri?.let { uri ->
            val file = File(getRealPathFromUri(uri))
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            photoPart = MultipartBody.Part.createFormData("image", file.name, requestFile)
        }

        // Crear la instancia del servicio Retrofit
        val apiService = RetrofitClient.instance.create(ApiService::class.java)

        // Hacer la llamada a Retrofit
        photoPart?.let {
            val call = apiService.updateProfilePhoto(userId, it)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val userResponse = response.body()
                        userResponse?.let { jsonObject ->
                            val editor = sharedPreferences.edit()
                            val newPhoto = jsonObject.get("newPhoto").asString
                            editor.putString("UserPhoto", newPhoto)
                            editor.apply()
                        }
                        Toast.makeText(requireContext(), getString(R.string.profile_picture_uploaded_success), Toast.LENGTH_LONG).show()

                    } else {
                        Toast.makeText(requireContext(), getString(R.string.profile_picture_error), Toast.LENGTH_LONG).show()
                        Log.i("tagg","Error al actualizar la foto de perfil: ${response.errorBody()?.string()}")
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("tagg","Error en la solicitud: ${t.message}")
                }
            })
        }
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

    override fun onEventCompleted() {
        val metaViajes = sharedPreferences.getInt("UserMetaViajes", 0)
        val visitadosCount = visitados.size()

        if (metaViajes > 0) {
            val progressPercentage = (visitadosCount * 100) / metaViajes
            progressBar.max = 100 // La barra de progreso se establece en un rango de 0 a 100 para el porcentaje
            progressBar.progress = progressPercentage
        } else {
            progressBar.max = 100
            progressBar.progress = 0
        }

        visitadosText.text = getString(R.string.visited_objective_profile) + visitadosCount
        metaViajesText.text = getString(R.string.goal_per_year_profile) + metaViajes
    }

    override fun onEventFailed() {
        visitadosText.text = getString(R.string.visited_objective_profile) + 0
        progressBar.progress = 0
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