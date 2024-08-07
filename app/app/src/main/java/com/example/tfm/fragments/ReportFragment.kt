package com.example.tfm.fragments

import ApiService
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.tfm.R
import com.example.tfm.models.Reporte
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class ReportFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var editTitulo: EditText
    private lateinit var editComentario: EditText
    private lateinit var errorText: TextView
    private lateinit var buttonPost: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_report, container, false)

        editTitulo = rootView.findViewById(R.id.textReportTitulo)
        editComentario = rootView.findViewById(R.id.reportText)
        buttonPost = rootView.findViewById(R.id.reportPostButton)
        errorText = rootView.findViewById(R.id.reportTextError)
        buttonPost.setOnClickListener {
            if(editComentario.text.toString().isEmpty() || editTitulo.text.toString().isEmpty()){
                errorText.text = getString(R.string.fill_form_report)
                errorText.visibility = View.VISIBLE
            }
            else{
                errorText.visibility = View.INVISIBLE
                postReporte()
            }
        }

        return rootView
    }

    private fun postReporte(){
        val sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("UserId", -1)
        val calendar = Calendar.getInstance()
        val timestamp = calendar.timeInMillis
        val reporte = Reporte(userId, timestamp ,editTitulo.text.toString(), editComentario.text.toString())

        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call: Call<JsonObject> = apiService.enviarReporte(reporte)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val reporteResponse = response.body()
                    if (reporteResponse != null) {
                        Log.d("tagg", "Reporte enviado exitosamente: $reporteResponse")
                        // Manejar la respuesta aquí (por ejemplo, mostrar un mensaje al usuario)
                    }
                } else {
                    Log.e("tagg", "Error en la respuesta del servidor: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("tagg", "Error al enviar el reporte: ${t.message}")
            }
        })
    }

}