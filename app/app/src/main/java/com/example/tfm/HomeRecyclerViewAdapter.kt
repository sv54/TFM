package com.example.tfm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfm.ui.home.ItemListaDestino

class HomeRecyclerViewAdapter(private var itemList: MutableList<ItemListaDestino> = mutableListOf()) : RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitulo: TextView = view.findViewById(R.id.item_titulo)
        val textViewPuntuacion: TextView = view.findViewById(R.id.item_puntuacion)
        val textViewVisitas: TextView = view.findViewById(R.id.item_popularidad)
        val textViewPais: TextView = view.findViewById(R.id.item_pais)
        val image: ImageView = view.findViewById(R.id.previewImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_destino_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewTitulo.text = itemList[position].titulo
        holder.textViewPuntuacion.text = itemList[position].puntuaciones.toString()
        holder.textViewVisitas.text = itemList[position].visitas.toString()
        holder.textViewPais.text = itemList[position].pais
        Glide.with(holder.itemView.context)
            .load(itemList[position].imagen)
            .into(holder.image)
    }

    override fun getItemCount(): Int = itemList.size

    fun updateItems(newItems: MutableList<ItemListaDestino>) {
        itemList = newItems
        notifyDataSetChanged()  // Notificar al RecyclerView que los datos han cambiado
    }
}