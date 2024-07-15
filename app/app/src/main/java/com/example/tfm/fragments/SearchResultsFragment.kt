package com.example.tfm.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.models.ItemListaDestino
import com.example.tfm.R
import com.example.tfm.adapters.SearchRecyclerViewAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [SearchResultsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchResultsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchRecyclerViewAdapter
    private lateinit var searchResults: MutableList<ItemListaDestino>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_results, container, false)

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.searchRecyclerViewResults)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize your search results list
        searchResults = mutableListOf(
//            ItemListaDestino(
//                id = 1,
//                titulo = "Madrid",
//                puntuaciones = 4.0f,
//                visitas = 522,
//                pais = "España",
//                imagen = "http://192.168.1.126:3000/public/imgDestination/Madrid1.png"
//            ),
//            ItemListaDestino(
//                id = 5,
//                titulo = "Roma",
//                puntuaciones = 4.0f,
//                visitas = 270,
//                pais = "Italia",
//                imagen = "http://192.168.1.126:3000/public/imgDestination/Roma1.jpg"
//            )
        )
        // Initialize the adapter with the search results list
        adapter = SearchRecyclerViewAdapter(searchResults)
        recyclerView.adapter = adapter

        return view
    }

    fun updateSearchResults(newResults: MutableList<ItemListaDestino>) {
        searchResults.clear()
        searchResults.addAll(newResults)
        adapter.updateItems(newResults)
        Log.d("tagg", "adapter: " + adapter.itemCount.toString())
    }
}