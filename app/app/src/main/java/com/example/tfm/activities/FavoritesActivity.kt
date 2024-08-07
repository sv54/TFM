package com.example.tfm.activities

import ApiService
import SharedPreferencesManager
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
import com.example.tfm.OnItemClickListener
import com.example.tfm.R
import com.example.tfm.adapters.HomeRecyclerViewAdapter
import com.example.tfm.fragments.HomeFragment
import com.example.tfm.models.ItemListaDestino
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritesActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var listaDestinos: List<Int>
    private lateinit var recyclerView: RecyclerView
    private lateinit var cardViewHeader: CardView
    private lateinit var adapter: HomeRecyclerViewAdapter
    private var destinosResponse = mutableListOf<ItemListaDestino>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        setSupportActionBar(findViewById(R.id.my_toolbar_favorite))
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = getString(R.string.favorits_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        cardViewHeader = findViewById(R.id.cardViewHeaderFav)


        recyclerView = findViewById(R.id.favorite_main_content)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HomeRecyclerViewAdapter(mutableListOf(), this)
        recyclerView.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        val jsonListaDestinos = SharedPreferencesManager.getFavorites(this)
        val sinDestinosText = findViewById<TextView>(R.id.textSinDestinosFavoritos)
        listaDestinos = SharedPreferencesManager.getFavoriteIds(jsonListaDestinos)
        if(listaDestinos.isNotEmpty()){

            getDestinos(listaDestinos)

            sinDestinosText.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            cardViewHeader.visibility = View.VISIBLE
        }
        else{
            sinDestinosText.visibility = View.VISIBLE
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
        val call = apiService.getSelectedDestinos(ids)
        call.enqueue(object : Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                if (response.isSuccessful) {
                    val destinosArray = response.body()
                    Log.d("tagg", "Destinos array: $destinosArray")
                    destinosArray?.let {
                        destinosResponse.clear()
                        for (i in 0 until it.size()) {
                            val destino = it.get(i).asJsonObject
                            val itemDestino: ItemListaDestino = ItemListaDestino();
                            itemDestino.id = destino.get("id").asInt
                            itemDestino.titulo = destino.get("titulo").asString
                            if(destino.get("numPuntuaciones").asInt <= 0){
                                itemDestino.puntuaciones = 0.0f
                            }
                            else{
                                itemDestino.puntuaciones = (destino.get("sumaPuntuaciones").asFloat/destino.get("numPuntuaciones").asFloat).toFloat()
                                itemDestino.puntuaciones = String.format("%.2f", itemDestino.puntuaciones).replace(',', '.').toFloat()
                            }
                            itemDestino.imagen = destino.get("imagen").asString
                            itemDestino.visitas = destino.get("numVisitas").asInt
                            itemDestino.pais = destino.get("nombrePais").asString
                            destinosResponse.add(itemDestino)
                        }
                        adapter.updateItems(destinosResponse)

                    }
                    // Aquí puedes manejar el objeto destinos
                } else {
                    Log.e("tagg", "Error al obtener favoritos" + response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                // Manejar el fallo de la llamada
                Log.e("tagg", "Error al obtener favoritos" + t.message)

            }
        })
    }

    override fun onItemClick(item: ItemListaDestino) {
        val destinoIdExtra = "DESTINOID"
        val destinoTituloExtra = "DESTINOTITULO"
        val intent = Intent(this, DestinoActivity::class.java).apply {
            putExtra(destinoTituloExtra, item.titulo)
            putExtra(destinoIdExtra, item.id)
        }
        startActivity(intent)
    }
}