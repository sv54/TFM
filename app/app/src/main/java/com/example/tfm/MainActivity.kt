package com.example.tfm

import ApiService
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.tfm.databinding.ActivityMainBinding
import com.example.tfm.ui.SearchResultsFragment
import com.example.tfm.ui.home.HomeFragment
import com.example.tfm.ui.home.ItemListaDestino
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var currentFragment: Fragment? = null
    private var searchResultsFragment: SearchResultsFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

//
//        binding.appBarMain.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_explore, R.id.nav_filter, R.id.nav_history, R.id.nav_help, R.id.nav_settings, R.id.nav_privacy, R.id.nav_terms
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        // Listener para cambiar la visibilidad del SearchView
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val searchView = binding.appBarMain.toolbar.findViewById<androidx.appcompat.widget.SearchView>(R.id.search_view)
            if (destination.id == R.id.nav_explore) {
                searchView.visibility = View.VISIBLE
                setupSearchViewListener(searchView)
            } else {
                searchView.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setupSearchViewListener(searchView: androidx.appcompat.widget.SearchView) {
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    performSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            searchResultsFragment = SearchResultsFragment()
            val homeFragment = HomeFragment()
            currentFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
            if (hasFocus) {
                // Mostrar el SearchResultsFragment al obtener el foco del SearchView
                supportFragmentManager.commit {
                    hide(homeFragment)
                    show(searchResultsFragment!!)
                    add(R.id.nav_host_fragment_content_main, searchResultsFragment!!)
                    addToBackStack(null)  // Agregar al back stack para poder volver atrás
                }
            } else {
                // Mostrar el fragmento actual (HomeFragment) si se pierde el foco del SearchView
                supportFragmentManager.commit {
                    hide(searchResultsFragment!!)
                    show(homeFragment)
                    remove(searchResultsFragment!!)  // Si se está mostrando el SearchResultsFragment, remuévelo
                    addToBackStack(null)  // Agregar al back stack para poder volver atrás
                }
            }
        }

        searchView.setOnCloseListener {
            // Mostrar el fragmento inicial (HomeFragment) y ocultar el SearchResultsFragment
            supportFragmentManager.popBackStack()
            currentFragment?.let {
                supportFragmentManager.commit {
                    show(it)
                }
            }
            false
        }


    }
    private fun performSearch(query: String){
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
                            itemDestino.puntuaciones = (element.asJsonObject.get("sumaPuntuaciones").asInt/element.asJsonObject.get("numPuntuaciones").asInt).toFloat()
                            itemDestino.puntuaciones = String.format("%.2f", itemDestino.puntuaciones).replace(',', '.').toFloat()
                        }
                        itemDestino.visitas = element.asJsonObject.get("numVisitas").asInt
                        itemDestino.pais = element.asJsonObject.get("nombrePais").asString
                        itemDestino.imagen = element.asJsonObject.get("imagen").asString
                        results.add(itemDestino)
                    }
                    Log.d("tagg", results.toString())

                    searchResultsFragment?.updateSearchResults(results)
                } else {
                    Log.e("tagg", "Error en la respuesta: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.e("tagg", "Error en la solicitud: ${t.message}")
            }
        })
    }
}