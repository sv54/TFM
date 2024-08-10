package com.example.tfm.fragments

import ApiService
import RetrofitClient
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.ApiListener
import com.example.tfm.models.ItemComment
import com.example.tfm.R
import com.example.tfm.adapters.CommentsAdapter
import com.example.tfm.adapters.HomeRecyclerViewAdapter
import com.example.tfm.models.ItemListaDestino
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommentFragment : Fragment(), ApiListener, CommentsAdapter.OnLoadMoreListener{
    private lateinit var recyclerView: RecyclerView
    //private lateinit var adapter: CommentsAdapter
    private var actualIndex = 0
    private var destinoId = -1

//    private var commentsList: MutableList<ItemComment> = mutableListOf()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_comment, container, false)

        recyclerView = rootView.findViewById(R.id.recyclerComment)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = CommentsAdapter(mutableListOf())
        recyclerView.adapter = adapter
        adapter.setOnLoadMoreListener(this)

        destinoId = arguments?.getInt("destinoId")!!
        commentsList = mutableListOf()
        getComments(actualIndex)

        return rootView
    }

    override fun onResume() {
        super.onResume()
    }




    override fun onEventCompleted() {
    }

    override fun onEventFailed() {
    }

    override fun onLoadMore() {
        getComments(actualIndex)
    }
    private fun getComments(index: Int){

        Log.d("tagg", "Pedimos comentarios de destino con id $destinoId y index $actualIndex")
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.getDestinoComentarios(destinoId, actualIndex)
        call.enqueue(object : Callback<JsonArray>{
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                if(response.isSuccessful){
                    actualIndex += 1
                    val commentslistBody = response.body()
                    Log.d("tagg", "Commments array: $commentslistBody")
                    commentsList.clear()
                    commentslistBody?.let{
                        for(i in 0 until it.size()){
                            val comment = it.get(i).asJsonObject
                            val itemComment: ItemComment = ItemComment()
                            itemComment.id = comment.get("id").asInt
                            itemComment.comment = comment.get("texto").asString
                            itemComment.permissionExtra = comment.get("permisoExtraInfo").asInt
                            itemComment.days = comment.get("estanciaDias").asInt
                            itemComment.expenses = comment.get("dineroGastado").asInt
                            itemComment.rate = comment.get("valoracion").asInt
                            itemComment.username = comment.get("nombreUsuario").asString
                            itemComment.userImage = comment.get("fotoPerfil").asString
                            commentsList.add(itemComment)
                        }
                        if(commentsList.size < 10){
                            adapter.moreComments = false
                        }
                        adapter.addItems(commentsList)

                    }
                }
                else{
                    Log.i("tagg", "holaFailure")
                    onEventFailed()
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                t.message?.let { Log.e("tagg", it) }
                onEventFailed()
            }
        })

    }

    companion object{
        private var commentsList: MutableList<ItemComment> = mutableListOf()
        private lateinit var adapter: CommentsAdapter

        fun addComment(newComment: ItemComment){
            commentsList.add(newComment)
            adapter.addItems(mutableListOf(newComment))

        }
    }




}