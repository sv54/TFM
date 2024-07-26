package com.example.tfm.fragments

import ApiService
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.tfm.ApiListener
import com.example.tfm.R
import com.example.tfm.activities.LoginRegisterActivity
import com.example.tfm.activities.MainActivity
import com.example.tfm.models.UserLoginData
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.mindrot.jbcrypt.BCrypt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment(), ApiListener {

    private lateinit var registerText: TextView
    private lateinit var logIn: Button
    private lateinit var userLoginData: UserLoginData

    private var userId = -1
    private var finalUsername = ""
    private var finalEmail = ""
    private var finalMetaViajes = 0
    private var finalTokenSesion = ""
    private var finalExpSesion = ""
    private var finalFotoPerfil = ""
    private var finalPaisId = 0
    private var finalPaisNombre = ""
    private var finalPaisISO = ""
    private var finalContinenteNombre = ""
    private var finalContinenteId = 0

    private var visitados: JsonArray = JsonArray()
    private var favoritos: JsonArray = JsonArray()
    private var historial: JsonArray = JsonArray()
    private var recomendados: JsonArray = JsonArray()

    private lateinit var errorTextView:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_login, container, false)

        errorTextView = rootView.findViewById(R.id.textErrorLogin)

        registerText = rootView.findViewById(R.id.textRegister)
        registerText.setOnClickListener{
            (activity as? LoginRegisterActivity)?.replaceFragment(RegisterFragment())
        }

        logIn = rootView.findViewById(R.id.buttonLogin)
        logIn.setOnClickListener {
            val email = rootView.findViewById<EditText>(R.id.editEmailLogin).text.toString()
            var password = rootView.findViewById<EditText>(R.id.editPasswordLogin).text.toString()
            //password = hashPassword(password, generateSalt())
            Log.i("tagg", password)
            userLoginData = UserLoginData(email, password)

            login(userLoginData)


        }




        return rootView
    }

    private fun login(userData: UserLoginData){
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.login(userData)
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
//                        finalTokenSesion = jsonObject.get("tokenSesion").asString.toString()
//                        finalExpSesion = jsonObject.get("expSesion").asString.toString()
                        finalMetaViajes = jsonObject.get("metaViajes").asInt
                        finalPaisId = jsonObject.get("paisId").asInt
                        finalPaisNombre = jsonObject.get("paisNombre").asString
                        finalPaisISO = jsonObject.get("paisIso").asString
                        finalContinenteNombre = jsonObject.get("continenteNombre").asString
                        finalContinenteId = jsonObject.get("continenteId").asInt

                        favoritos = jsonObject.getAsJsonArray("favoritos")
                        visitados = jsonObject.getAsJsonArray("visitados")
                        historial = jsonObject.getAsJsonArray("historial")
                        recomendados = jsonObject.getAsJsonArray("recomendados")
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

    override fun onEventCompleted() {
        hideError()
        val sharedPreferences = requireContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val favoritosJson = gson.toJson(favoritos)
        val visitadosJson = gson.toJson(visitados)
        val historialJson = gson.toJson(historial)
        val recomendadosJson = gson.toJson(recomendados)

        val editor = sharedPreferences.edit()
        editor.putInt("UserId", userId)
        editor.putString("UserUsername", finalUsername)
        editor.putString("UserEmail", finalEmail)
        editor.putString("UserPhoto", finalFotoPerfil)
//        editor.putString("UserTokenSesion", finalTokenSesion)
//        editor.putString("UserExpSesion", finalExpSesion)
        editor.putInt("UserPaisId",finalPaisId)
        editor.putString("UserPaisNombre",finalPaisNombre)
        editor.putString("UserPaisISO",finalPaisISO)
        editor.putString("UserContinenteNombre",finalContinenteNombre)
        editor.putInt("UserContinenteId",finalContinenteId)
        editor.putInt("UserMetaViajes", finalMetaViajes)
        editor.putString("UserFavoritos", favoritosJson)
        editor.putString("UserVisitados", visitadosJson)
        editor.putString("UserHistorial", historialJson)
        editor.putString("UserRecomendados", recomendadosJson)
        editor.apply()

        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onEventFailed() {
        setErrorMessage("El usuario o la contraseña no existen")
    }

    private fun setErrorMessage(message: String) {
        errorTextView.text = message
        errorTextView.setTextColor(Color.RED)

    }
    private fun hideError() {
        errorTextView.text = ""
    }

    private fun generateSalt(): String {
        return BCrypt.gensalt()
    }

    private fun hashPassword(password: String, salt: String): String {
        return BCrypt.hashpw(password, salt)
    }

    private fun goToMainActivity(){
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }



}