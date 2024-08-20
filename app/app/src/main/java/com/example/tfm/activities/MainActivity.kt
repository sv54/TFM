package com.example.tfm.activities

import ApiService
import RetrofitClient
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.example.tfm.FragmentChangeListener
import com.example.tfm.R
import com.example.tfm.databinding.ActivityMainBinding
import com.example.tfm.fragments.BottomSortOptions
import com.example.tfm.fragments.DestinoFragment
import com.example.tfm.fragments.HistoryFragment
import com.example.tfm.fragments.HomeFragment
import com.example.tfm.fragments.PrivacyPolicyFragment
import com.example.tfm.fragments.ReportFragment
import com.example.tfm.fragments.SettingsFragment
import com.example.tfm.fragments.TermsOfUseFragment
import com.example.tfm.models.ItemListaDestino
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
    private lateinit var searchView:SearchView
    private lateinit var fab: FloatingActionButton
    private lateinit var menuImageProfile: ImageView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //supportFragmentManager.beginTransaction().replace(R.id.main_content, HomeFragment()).commit()

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setCheckedItem(R.id.nav_explore)
        toggle = ActionBarDrawerToggle(
            this, drawerLayout
            //, findViewById(R.id.my_toolbar)
            , R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        toggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //drawerLayout.addDrawerListener(toggle)


        fab = findViewById(R.id.fabOrdenar)
        fab.setOnClickListener {
            val bottomSheetFragment = BottomSortOptions()
            hideKeyboard()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }


        //Desactivar el boton de ordenacion si el menu lateral esta abierto
        fab.isEnabled = !drawerLayout.isDrawerOpen(navView)
        drawerLayout.addDrawerListener(object: DrawerLayout.DrawerListener{
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                fab.hide()
            }

            override fun onDrawerOpened(drawerView: View) {
                fab.hide()
            }

            override fun onDrawerClosed(drawerView: View) {
                checkFragment()
            }

            override fun onDrawerStateChanged(newState: Int) {
            }
        })
        if(drawerLayout.isDrawerOpen(navView)){
        }


        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    sharedPreferences.edit().clear().apply()
                    val sharedPreferencesSettings = PreferenceManager.getDefaultSharedPreferences(this)
                    sharedPreferencesSettings.edit().clear().apply()
                    val intent = Intent(this, LoginRegisterActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                R.id.nav_settings -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, SettingsFragment()).commit()
                }
                R.id.nav_explore -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, HomeFragment()).commit()
                }
                R.id.nav_help -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, ReportFragment()).commit()
                }
                R.id.nav_history -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, HistoryFragment()).commit()
                }
                R.id.nav_privacy -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, PrivacyPolicyFragment()).commit()
                }
                R.id.nav_terms -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, TermsOfUseFragment()).commit()
                    checkFragment()

                }
                // Handle menu item selections here
            }
            drawerLayout.closeDrawers()
            true
        }



        // Add an OnBackStackChangedListener to detect fragment changes
        supportFragmentManager.addOnBackStackChangedListener {
            checkFragment()
        }
        checkFragment()

        val headerView = navView.getHeaderView(0)
        val textMenuUsername = headerView.findViewById<TextView>(R.id.textViewName)
        val textMenuEmail = headerView.findViewById<TextView>(R.id.textViewEmail)
        menuImageProfile = headerView.findViewById<ImageView>(R.id.menuFotoPerfil)

        val dataUsername = sharedPreferences.getString("UserUsername", "")
        val dataEmail = sharedPreferences.getString("UserEmail", "")
        val dataProfileImage = sharedPreferences.getString("UserPhoto", "")

        textMenuUsername.text = dataUsername
        textMenuEmail.text = dataEmail

        if(dataProfileImage == "" || dataProfileImage!!.contains("sinFoto") || dataProfileImage.contains("SinFoto") ){
            menuImageProfile.setImageResource(R.drawable.ic_empty_photo)
        }
        else{
            Glide.with(menuImageProfile.context)
                .load(dataProfileImage)
                .into(menuImageProfile)
        }

        headerView.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        val dataProfileImage = sharedPreferences.getString("UserPhoto", "")
        if(dataProfileImage == "" || dataProfileImage!!.contains("sinFoto") || dataProfileImage.contains("SinFoto")){
            menuImageProfile.setImageResource(R.drawable.ic_empty_photo)
        }
        else{
            Glide.with(menuImageProfile.context)
                .load(dataProfileImage)
                .into(menuImageProfile)
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
        searchView = searchMenuItem?.actionView as SearchView
        checkFragment()

        searchView.setOnClickListener {
            fab.hide()
        }

        // Configurar el SearchView
        searchView.queryHint = getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    Log.i("tagg", it)
                    performSearch(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    fab.show() // Mostrar el FAB si no hay texto de búsqueda
                } else {
                    fab.hide() // Ocultar el FAB mientras se escribe
                }
                return true
            }
        })

        searchView.setOnCloseListener {
            fab.show()
            val fragmento = supportFragmentManager.findFragmentById(R.id.main_content)

            if (fragmento is HomeFragment) {
                fragmento.onCloseSearch()
            }
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

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus // Obtiene la vista actual que tiene el foco

        if (view == null) {
            view = View(this) // Crea una nueva vista si no hay una vista con foco
        }

        imm.hideSoftInputFromWindow(view.windowToken, 0) // Oculta el teclado
    }

    private fun checkFragment() {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_content)
        if (fragment is DestinoFragment) {
            Log.i("tagg", "Is Destino Fragment")
            fab.hide()
            searchMenuItem?.isVisible = false
            supportActionBar?.setDisplayShowTitleEnabled(true)
        } else if(fragment is HomeFragment){
            searchMenuItem?.isVisible = true
            fab.show()
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setTitle("")
        }
        else if (fragment is SettingsFragment){
            fab.hide()
            searchMenuItem?.isVisible = false
            supportActionBar?.setDisplayShowTitleEnabled(true)
            supportActionBar?.title = getString(R.string.setting_toolbar_title)
        }
        else if (fragment is ReportFragment){
            fab.hide()
            searchMenuItem?.isVisible = false
            supportActionBar?.setDisplayShowTitleEnabled(true)
            supportActionBar?.title = getString(R.string.report_toolbar_title)
        }
        else if (fragment is HistoryFragment){
            fab.hide()
            searchMenuItem?.isVisible = false
            supportActionBar?.setDisplayShowTitleEnabled(true)
            supportActionBar?.title = getString(R.string.toolbar_history)
        }
        else{
            fab.hide()
            searchMenuItem?.isVisible = false
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setTitle("")
        }

    }

}
