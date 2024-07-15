package com.example.tfm

import ApiService
import RetrofitClient
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tfm.databinding.ActivityMain2Binding
import com.example.tfm.databinding.ActivityMainBinding
import com.example.tfm.ui.BottomSortOptions
import com.example.tfm.ui.home.HomeFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), FragmentChangeListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private var searchMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //supportFragmentManager.beginTransaction().replace(R.id.main_content, HomeFragment()).commit()

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(
            this, drawerLayout
            //, findViewById(R.id.my_toolbar)
            ,R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        toggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //drawerLayout.addDrawerListener(toggle)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                // Handle menu item selections here
            }
            drawerLayout.closeDrawers()
            true
        }

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val bottomSheetFragment = BottomSortOptions()
            hideKeyboard()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }


        // Add an OnBackStackChangedListener to detect fragment changes
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.main_content)
            if (fragment is DestinoFragment) {
                fab.hide()
                searchMenuItem?.isVisible = false
                supportActionBar?.setDisplayShowTitleEnabled(true)
            } else {
                searchMenuItem?.isVisible = true
                fab.show()
                supportActionBar?.setDisplayShowTitleEnabled(false)
                supportActionBar?.setTitle("")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_content)
        if(toggle.onOptionsItemSelected(item)){
            hideKeyboard()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        searchMenuItem = menu.findItem(R.id.action_search)
        val searchView = searchMenuItem?.actionView as SearchView

        // Configurar el SearchView
        searchView.queryHint = "Buscar..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    Log.i("tagg", it)
                    performSearch(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        searchView.setOnCloseListener {
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
                        val itemDestino = ItemListaDestino().apply {
                            id = element.asJsonObject.get("id").asInt
                            titulo = element.asJsonObject.get("titulo").asString
                            puntuaciones = if (element.asJsonObject.get("numPuntuaciones").asInt <= 0) {
                                0.0f
                            } else {
                                (element.asJsonObject.get("sumaPuntuaciones").asFloat / element.asJsonObject.get("numPuntuaciones").asFloat).toFloat().also {
                                    String.format("%.2f", it).replace(',', '.').toFloat()
                                }
                            }
                            visitas = element.asJsonObject.get("numVisitas").asInt
                            pais = element.asJsonObject.get("nombrePais").asString
                            imagen = element.asJsonObject.get("imagen").asString
                        }
                        results.add(itemDestino)
                    }
                    Log.i("tagg", "results on search " + results.size)
                    HomeFragment.updateList(results)
                } else {
                    Log.e("tagg", "Error en la respuesta: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.e("tagg", "Error en la solicitud: ${t.message}")
            }
        })
    }

    override fun onFragmentChange(id: Int, destinoTitulo: String) {
        val fragment = DestinoFragment.newInstance(id)
        supportActionBar?.title = destinoTitulo
        hideKeyboard()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_content, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showBackArrow() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back) // Asegúrate de tener un icono de flecha en tus drawables
        toggle.isDrawerIndicatorEnabled = false
        toggle.syncState()
        toggle.toolbarNavigationClickListener = View.OnClickListener {
            onBackPressed()
        }
    }

    private fun showHamburgerIcon() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        toggle.isDrawerIndicatorEnabled = true
        toggle.toolbarNavigationClickListener = null
        toggle.syncState()
    }

    fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus // Obtiene la vista actual que tiene el foco

        if (view == null) {
            view = View(this) // Crea una nueva vista si no hay una vista con foco
        }

        imm.hideSoftInputFromWindow(view.windowToken, 0) // Oculta el teclado
    }

}
