package com.example.tfm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfm.OnItemVisitedClickListener
import com.example.tfm.R
import com.example.tfm.models.ItemDestinoVisitado
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VisitedAdapter(private var itemList: MutableList<ItemDestinoVisitado> = mutableListOf(), private val listener: OnItemVisitedClickListener) : RecyclerView.Adapter<VisitedAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val imageView: ImageView = view.findViewById(R.id.previewImageVisitado)
        val textTitulo: TextView = view.findViewById(R.id.item_titulo_visitado)
        val textPais: TextView = view.findViewById(R.id.item_pais_visitado)
        val textFecha: TextView = view.findViewById(R.id.item_fecha_visitado)
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            listener.onItemClick(itemList[adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_destino_visited, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        Glide.with(holder.itemView.context)
            .load(item.imagen)  // Aquí se carga la imagen usando el path
            .into(holder.imageView)

        holder.textTitulo.text = item.titulo
        holder.textPais.text = item.pais
        val date = Date(item.fecha * 1000)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
        holder.textFecha.text = dateFormat.toString()

    }

    override fun getItemCount(): Int = itemList.size

    // Método para actualizar los elementos del adaptador
    fun updateItems(newItems: MutableList<ItemDestinoVisitado>) {
        itemList = newItems
        notifyDataSetChanged()
    }

    fun addItem(newItem: ItemDestinoVisitado) {
        itemList.add(newItem)
        notifyItemInserted(0)
    }
}


