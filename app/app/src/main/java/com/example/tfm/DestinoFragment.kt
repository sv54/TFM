package com.example.tfm

import ApiService
import CarouselAdapter
import RetrofitClient
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// Definir el nombre de la clave como una constante de tipo String
private const val ARG_DESTINO_ID = "destino_id"

/**
 * A simple [Fragment] subclass.
 * Use the [DestinoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DestinoFragment : Fragment(), ApiListener {


    private var destinoId: Int? = null
    private lateinit var destino: ItemDestino
    private var carouselAdapter: CarouselAdapter? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var  adapter: CarouselAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            destinoId = it.getInt(ARG_DESTINO_ID)
        }

        getDestinoData()

        //Toast.makeText(context, "Item $destinoId clicked", Toast.LENGTH_SHORT).show()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_destino, container, false)
        recyclerView = rootView.findViewById(R.id.recycler)
        carouselAdapter = CarouselAdapter(requireContext(), mutableListOf())
        recyclerView.adapter = carouselAdapter
        
        return rootView
    }

    fun getDestinoData(){
        Log.d("tagg", "Pedimos datos del destino con id $destinoId")
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.getDestinoId(destinoId!!)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val destinoBody = response.body()
                    Log.d("tagg", "Destino datos: $destinoBody")
                    destinoBody?.let { jsonObject ->
                        val itemDestino = ItemDestino().apply {
                            id = jsonObject.get("id").asInt
                            titulo = jsonObject.get("titulo").asString
                            descripcion = jsonObject.get("descripcion").asString
                            if(destinoBody.get("numPuntuaciones").asInt <= 0){
                                puntuaciones = 0.0f
                            }
                            else{
                                puntuaciones = (destinoBody.get("sumaPuntuaciones").asFloat/destinoBody.get("numPuntuaciones").asFloat).toFloat()
                                puntuaciones = String.format("%.2f", puntuaciones).replace(',', '.').toFloat()
                            }
                            if(destinoBody.get("gastoTotal").asInt <= 0){
                                gastoDia = 0
                            }
                            else{
                                gastoDia = (destinoBody.get("gastoTotal").asFloat/destinoBody.get("diasEstanciaTotal").asFloat).toInt()
                            }
                            indiceSeguridad = jsonObject.get("indiceSeguridad").asInt
                            moneda = jsonObject.get("moneda").asString
                            clima = jsonObject.get("clima").asString
                            visitas = destinoBody.get("numVisitas").asInt
                            pais = jsonObject.get("nombrePais").asString

                            // Para las imágenes, suponiendo que es un array de strings
                            val imagenesJsonArray = jsonObject.getAsJsonArray("imagenes")
                            imagenes = mutableListOf<String>().apply {
                                imagenesJsonArray.forEach { jsonElement ->
                                    add(jsonElement.asString)
                                }
                            }
                        }
                        destino = itemDestino

                        Log.d("tagg", itemDestino.toString())
                        onEventCompleted()
                        //itemDestino.pais = jsonObject.get("nombrePais").asString
                    }
                } else {
                    // Manejar error en la respuesta
                    Log.e("tagg", "Error en la respuesta: ${response.message()}")
                    onEventFailed()

                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Manejar fallo en la solicitud
                Log.e("tagg", "Error en la solicitud: ${t.message}")
            }
        })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param destinoId Parameter 1.
         * @return A new instance of fragment DestinoFragment.
         */
        @JvmStatic
        fun newInstance(destinoId: Int) =
            DestinoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_DESTINO_ID, destinoId)
                }
            }
    }

    override fun onEventCompleted() {
        carouselAdapter?.updateItems(destino.imagenes)
    }

    override fun onEventFailed() {
        requireActivity().supportFragmentManager.popBackStack()
    }
}
