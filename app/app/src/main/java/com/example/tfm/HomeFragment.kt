package com.example.tfm.ui.home

import ApiService
import RetrofitClient
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.FragmentChangeListener
import com.example.tfm.HomeRecyclerViewAdapter
import com.example.tfm.ItemListaDestino
import com.example.tfm.MainActivity
import com.example.tfm.OnItemClickListener
import com.example.tfm.R
import com.example.tfm.databinding.FragmentHomeBinding
import com.example.tfm.ui.BottomSortOptions
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        homeRecyclerViewAdapter = HomeRecyclerViewAdapter(destinos, this)
        recyclerView.adapter = homeRecyclerViewAdapter

        cargarDestinos()

        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentList = fragmentManager.fragments

        for (fragment in fragmentList) {
            val tag = fragment.id // Aquí obtienes el tag asociado al Fragmento
            // Puedes hacer lo que necesites con el tag, por ejemplo, imprimirlo
            Log.d("FragmentTag", "Fragment tag: $tag")
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
        fragmentChangeListener.onFragmentChange(item.id)
    }

}