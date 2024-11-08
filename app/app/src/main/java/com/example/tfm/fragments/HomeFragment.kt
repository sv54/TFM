package com.example.tfm.fragments

import ApiService
import RetrofitClient
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tfm.FragmentChangeListener
import com.example.tfm.adapters.HomeRecyclerViewAdapter
import com.example.tfm.models.ItemListaDestino
import com.example.tfm.activities.MainActivity
import com.example.tfm.OnItemClickListener
import com.example.tfm.R
import com.example.tfm.activities.DestinoActivity
import com.example.tfm.databinding.FragmentHomeBinding
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), OnItemClickListener {


    private var _binding: FragmentHomeBinding? = null
    private var lastDy = 0
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    private lateinit var recyclerView: RecyclerView
    private lateinit var fragmentChangeListener: FragmentChangeListener
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        homeRecyclerViewAdapter = HomeRecyclerViewAdapter(destinos, this)
        recyclerView.adapter = homeRecyclerViewAdapter
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        cargarDestinos()

        swipeRefreshLayout.setOnRefreshListener {
            cargarDestinos()
            Handler(Looper.getMainLooper()).postDelayed({
                swipeRefreshLayout.isRefreshing = false
            }, 750)
        }

        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentList = fragmentManager.fragments

        for (fragment in fragmentList) {
            val tag = fragment.id
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChangeListener) {
            fragmentChangeListener = context
        } else {
            throw RuntimeException("$context must implement FragmentChangeListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }




    private fun cargarDestinos() {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.destinosAll()
        Log.i("tagg", "Pedimos destinos para HomeFragment")
        call.enqueue(object : Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                if (response.isSuccessful) {
                    val destinosArray = response.body()
                    Log.d("tagg", "Destinos array: $destinosArray")
                    destinosArray?.let {
                        destinos.clear()
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
                            destinos.add(itemDestino)
                        }
                        homeRecyclerViewAdapter.updateItems(destinos)

                    }
                } else {
                    // Manejar error en la respuesta
                    Log.e("tagg", "Error en la respuesta: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                // Manejar fallo en la solicitud
                Log.e("tagg", "Error en la solicitud: ${t.message}")
            }
        })
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        private var destinos: MutableList<ItemListaDestino> = mutableListOf()
        private lateinit var homeRecyclerViewAdapter: HomeRecyclerViewAdapter

        fun updateList(newResults: MutableList<ItemListaDestino>) {
            destinos.clear()
            destinos.addAll(newResults)
            homeRecyclerViewAdapter.updateItems(newResults)

        }
    }

    override fun onItemClick(item: ItemListaDestino) {
        val destinoIdExtra = "DESTINOID"
        val destinoTituloExtra = "DESTINOTITULO"
        val intent = Intent(requireContext(), DestinoActivity::class.java).apply {
            putExtra(destinoTituloExtra, item.titulo)
            putExtra(destinoIdExtra, item.id)
        }
        startActivity(intent)
        //fragmentChangeListener.onFragmentChange(item.id, item.titulo)
    }

    public fun onCloseSearch(){
        cargarDestinos()
    }

}