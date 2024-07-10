package com.example.tfm

import ApiService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tfm.databinding.ActivityMainBinding
import com.example.tfm.ui.BottomSortOptions
import com.example.tfm.ui.home.HomeFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.JsonArray

class MainActivity : AppCompatActivity(), FragmentChangeListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.my_toolbar))
        getSupportActionBar()?.setDisplayShowTitleEnabled(false);

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        // Configurar el listener para abrir/cerrar el drawer
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, findViewById(R.id.my_toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportFragmentManager.beginTransaction().replace(R.id.main_content, HomeFragment()).commit()

        // Configurar el listener para los items del menú
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
//                R.id.nav_item1 -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.main_content, Fragment1())
//                        .commit()
//                }
//                R.id.nav_item2 -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.main_content, Fragment2())
//                        .commit()
//                }
                // Agregar más casos según sea necesario para otros elementos del menú
            }
            // Cerrar el drawer después de cambiar de fragmento
            drawerLayout.closeDrawers()
            true
        }
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val bottomSheetFragment = BottomSortOptions()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        // Configurar el SearchView
        searchView.queryHint = "Buscar..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    Log.i("tagg", query)
                }
                // Aquí puedes manejar la acción de búsqueda
                if(query != null && !query.isEmpty()){
                    performSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Aquí puedes manejar cambios en el texto de búsqueda
                return true
            }
        })

        // Configurar el listener para el ícono de cerrar (x)
        searchView.setOnCloseListener {
            // Aquí puedes manejar el cierre del SearchView
            false
        }

        return true
    }





    private fun performSearch(query: String) {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.buscarDestino(query)
        Log.i("tagg", "Pedimos destinos para la busqueda con: $query")
        call.enqueue(object : Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                if (response.isSuccessful) {
                    val jsonArray = response.body()

                    val results = mutableListOf<ItemListaDestino>()

                    jsonArray?.forEach { element ->
                        val itemDestino = ItemListaDestino()
                        itemDestino.id = element.asJsonObject.get("id").asInt
                        itemDestino.titulo = element.asJsonObject.get("titulo").asString
                        if(element.asJsonObject.get("numPuntuaciones").asInt <= 0){
                            itemDestino.puntuaciones = 0.0f
                        }
                        else{
                            itemDestino.puntuaciones = (element.asJsonObject.get("sumaPuntuaciones").asFloat/element.asJsonObject.get("numPuntuaciones").asFloat).toFloat()
                            itemDestino.puntuaciones = String.format("%.2f", itemDestino.puntuaciones).replace(',', '.').toFloat()
                        }
                        itemDestino.visitas = element.asJsonObject.get("numVisitas").asInt
                        itemDestino.pais = element.asJsonObject.get("nombrePais").asString
                        itemDestino.imagen = element.asJsonObject.get("imagen").asString
                        results.add(itemDestino)
                    }
                    Log.i("tagg", "results on search " + results.size)
                    HomeFragment.updateList(results)
                    //searchResultsFragment.updateSearchResults(results)
                } else {
                    Log.e("tagg", "Error en la respuesta: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.e("tagg", "Error en la solicitud: ${t.message}")
            }
        })
    }

    override fun onFragmentChange(id: Int) {
        // Aquí puedes pasar datos adicionales al fragmento destino si es necesario
        val fragment = DestinoFragment.newInstance(id)

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_content, fragment)
            .addToBackStack(null)
            .commit()
    }
}