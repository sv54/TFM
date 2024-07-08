import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.tfm.ItemListaDestino
import com.example.tfm.databinding.ActivityMainBinding
import com.example.tfm.ui.BottomSortOptions
import com.example.tfm.ui.SearchResultsFragment
import com.example.tfm.ui.home.HomeFragment
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.tfm.R

class MainActivity : AppCompatActivity(), BottomSortOptions.BottomSheetListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var searchResultsFragment: SearchResultsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        homeFragment = HomeFragment()
        searchResultsFragment = SearchResultsFragment()

        supportFragmentManager.commit {
            add(R.id.nav_host_fragment_content_main, homeFragment, "HomeFragment")
            hide(searchResultsFragment)
        }

        binding.appBarMain.fab.setOnClickListener {
            val bottomSheetFragment = BottomSortOptions()
            bottomSheetFragment.setListener(this)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSortOptionSelected(newResults: MutableList<ItemListaDestino>) {
        HomeFragment.updateList(newResults)
    }

    fun showSearchResults(query: String) {
        performSearch(query)
        supportFragmentManager.commit {
            hide(homeFragment)
            show(searchResultsFragment)
            addToBackStack(null)
        }
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

                    searchResultsFragment.updateSearchResults(results)
                } else {
                    Log.e("tagg", "Error en la respuesta: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.e("tagg", "Error en la solicitud: ${t.message}")
            }
        })
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}