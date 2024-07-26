package com.example.tfm.activities

import ApiService
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.OnItemVisitedClickListener
import com.example.tfm.R
import com.example.tfm.adapters.VisitedAdapter
import com.example.tfm.models.ItemDestinoVisitado
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VisistedActivity : AppCompatActivity(), OnItemVisitedClickListener {

    private lateinit var listaDestinos: List<Int>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VisitedAdapter
    private lateinit var cardViewHeader: CardView
    private var destinosResponse = mutableListOf<ItemDestinoVisitado>()
    private var userId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visisted)

        setSupportActionBar(findViewById(R.id.my_toolbar_visited))
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = "Visitados"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.visited_main_content)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = VisitedAdapter(mutableListOf(), this)
        recyclerView.adapter = adapter

        cardViewHeader = findViewById(R.id.cardViewHeaderVis)

        val sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)
        userId = sharedPreferences.getInt("UserId", -1 )
        if(userId == -1){
            onBackPressedDispatcher.onBackPressed()
        }

    }

    override fun onResume() {
        super.onResume()
        val textSinDestinos = findViewById<TextView>(R.id.textSinDestinosVisitados)

        val jsonListaDestinos = SharedPreferencesManager.getVisited(this)
        listaDestinos = SharedPreferencesManager.getVisitedIds(jsonListaDestinos)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Manejar el evento de clic del botón de la toolbar
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getDestinos(ids: List<Int>){
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.getVisitedDestinos(userId)
        call.enqueue(object : Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                if (response.isSuccessful) {
                    val destinosArray = response.body()
                    Log.d("tagg", "Destinos array: $destinosArray")
                    destinosArray?.let {
                        destinosResponse.clear()
                        for (i in 0 until it.size()) {
                            val destino = it.get(i).asJsonObject
                            val itemDestino = ItemDestinoVisitado();
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
                    Log.e("tagg", "Error al obtener visitados" + response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                // Manejar el fallo de la llamada
                Log.e("tagg", "Error al obtener visitados" + t.message)

            }
        })
    }

    override fun onItemClick(item: ItemDestinoVisitado) {
        val destinoIdExtra = "DESTINOID"
        val destinoTituloExtra = "DESTINOTITULO"
        val intent = Intent(this, DestinoActivity::class.java).apply {
            putExtra(destinoTituloExtra, item.titulo)
            putExtra(destinoIdExtra, item.id)
        }
        startActivity(intent)
    }

}