package com.example.tfm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class HomeRecyclerViewAdapter(private var itemList: MutableList<ItemListaDestino> = mutableListOf(), private val listener: OnItemClickListener) : RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val textViewTitulo: TextView = view.findViewById(R.id.item_titulo)
        val textViewPuntuacion: TextView = view.findViewById(R.id.item_puntuacion)
        val textViewVisitas: TextView = view.findViewById(R.id.item_popularidad)
        val textViewPais: TextView = view.findViewById(R.id.item_pais)
        val image: ImageView = view.findViewById(R.id.previewImage)
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            listener.onItemClick(itemList[adapterPosition])
        }
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

        if (position === itemList.size - 1) {
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            params.bottomMargin = 300 // last item bottom margin
            holder.itemView.layoutParams = params
        }
        else{
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            params.bottomMargin = 4
            holder.itemView.layoutParams = params
        }
    }

    override fun getItemCount(): Int = itemList.size

    fun updateItems(newItems: MutableList<ItemListaDestino>) {
        itemList = newItems
        notifyDataSetChanged()  // Notificar al RecyclerView que los datos han cambiado
    }
}