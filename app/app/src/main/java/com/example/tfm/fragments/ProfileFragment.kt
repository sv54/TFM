package com.example.tfm.fragments

import ApiService
import SharedPreferencesManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.tfm.ApiListener
import com.example.tfm.R
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment(), ApiListener {

    private lateinit var imageProfile: ImageView
    private lateinit var usernameText: TextView
    private lateinit var emailText: TextView
    private lateinit var favoritoButton: Button
    private lateinit var visitadosButton: Button
    private lateinit var metaViajesText: TextView
    private lateinit var visitadosText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferences: SharedPreferences

    private var visitados: JsonArray = JsonArray()
    private var favoritos: JsonArray = JsonArray()
    private var historial: JsonArray = JsonArray()


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

        val dataUsername = sharedPreferences.getString("UserUsername", "")
        val dataEmail = sharedPreferences.getString("UserEmail", "")
        val dataProfileImage = sharedPreferences.getString("UserPhoto", "")

        Log.i("tagg", "Favorite" + SharedPreferencesManager.getFavorites(requireContext()).toString())
        Log.i("tagg", "Visited" + SharedPreferencesManager.getVisited(requireContext()).toString())
        Log.i("tagg", "History" + SharedPreferencesManager.getHistory(requireContext()).toString())


        usernameText.text = dataUsername
        emailText.text = dataEmail

        if(dataProfileImage == ""){
            imageProfile.setImageResource(R.drawable.ic_empty_photo)
        }
        else{
            Glide.with(imageProfile.context)
                .load(dataProfileImage)
                .into(imageProfile)
        }


        val visitedList = SharedPreferencesManager.getVisited(requireContext())
        val metaViajes = sharedPreferences.getInt("UserMetaViajes", 0)
        if (metaViajes > 0) {
            val progressPercentage = (visitedList.size * 100) / metaViajes
            progressBar.max = 100 // La barra de progreso se establece en un rango de 0 a 100 para el porcentaje
            progressBar.progress = progressPercentage
        } else {
            progressBar.max = 100
            progressBar.progress = 0
        }

        visitadosText.text = "Visitados: " + visitedList.size
        metaViajesText.text = "Meta: $metaViajes"

        return rootView
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

        visitadosText.text = "Visitados: $visitadosCount"
        metaViajesText.text = "Meta: $metaViajes"
    }

    override fun onEventFailed() {
        visitadosText.text = "Visitados: ?"
        progressBar.progress = 0
    }


}