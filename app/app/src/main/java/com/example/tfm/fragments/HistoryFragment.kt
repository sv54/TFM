package com.example.tfm.fragments

import ApiService
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.OnItemHistoryClickListener
import com.example.tfm.R
import com.example.tfm.activities.DestinoActivity
import com.example.tfm.adapters.HistoryAdapter
import com.example.tfm.adapters.VisitedAdapter
import com.example.tfm.models.ItemDestinoHistory
import com.example.tfm.models.ItemDestinoVisitado
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HistoryFragment : Fragment(), OnItemHistoryClickListener {

    private lateinit var listaDestinos: List<Int>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private lateinit var cardViewHeader: CardView
    private var destinosResponse = mutableListOf<ItemDestinoHistory>()
    private var userId: Int = -1
    private lateinit var rootView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val sharedPreferences = requireContext().getSharedPreferences("MyPreferences",
            AppCompatActivity.MODE_PRIVATE
        )
        userId = sharedPreferences.getInt("UserId", -1 )
        if(userId == -1){
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_history, container, false)

        cardViewHeader = rootView.findViewById(R.id.cardViewHeaderHis)


        recyclerView = rootView.findViewById(R.id.history_main_content)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = HistoryAdapter(mutableListOf(), this)
        recyclerView.adapter = adapter
        return rootView
    }

    override fun onResume() {
        super.onResume()
        val textSinDestinos = rootView.findViewById<TextView>(R.id.textSinDestinosEnHistory)

        val jsonListaDestinos = SharedPreferencesManager.getHistory(requireContext())
        listaDestinos = SharedPreferencesManager.getHistoryIds(jsonListaDestinos)
        if(listaDestinos.isNotEmpty()){
            getDestinos(listaDestinos)
            textSinDestinos.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            cardViewHeader.visibility = View.VISIBLE
        }
        else{
            textSinDestinos.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            cardViewHeader.visibility = View.GONE
            adapter.updateItems(mutableListOf())
        }
    }

    private fun getDestinos(ids: List<Int>){
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.getHistoryDestinos(userId)
        call.enqueue(object : Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                if (response.isSuccessful) {
                    val destinosArray = response.body()
                    Log.d("tagg", "Destinos array: $destinosArray")
                    destinosArray?.let {
                        destinosResponse.clear()
                        for (i in 0 until it.size()) {
                            val destino = it.get(i).asJsonObject
                            val itemDestino = ItemDestinoHistory();
                            itemDestino.id = destino.get("id").asInt
                            itemDestino.titulo = destino.get("titulo").asString
                            itemDestino.imagen = destino.get("imagen").asString
                            itemDestino.pais = destino.get("nombrePais").asString
                            itemDestino.fecha = destino.get("fecha").asLong
                            destinosResponse.add(itemDestino)
                        }
                        adapter.updateItems(destinosResponse)

                    }
                    // Aquí puedes manejar el objeto destinos
                } else {
                    Log.e("tagg", "Error al obtener visitados " + response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                // Manejar el fallo de la llamada
                Log.e("tagg", "Error al obtener visitados" + t.message)

            }
        })
    }

    override fun onItemClick(item: ItemDestinoHistory) {
        val destinoIdExtra = "DESTINOID"
        val destinoTituloExtra = "DESTINOTITULO"
        val intent = Intent(requireActivity(), DestinoActivity::class.java).apply {
            putExtra(destinoTituloExtra, item.titulo)
            putExtra(destinoIdExtra, item.id)
        }
        startActivity(intent)
    }
}