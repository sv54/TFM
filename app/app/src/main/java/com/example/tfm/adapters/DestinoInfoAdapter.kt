package com.example.tfm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfm.models.ItemInfo
import com.example.tfm.R

class DestinoInfoAdapter(private var itemList: MutableList<ItemInfo> = mutableListOf()) : RecyclerView.Adapter<DestinoInfoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.icInfo)
        val textViewInfoType: TextView = view.findViewById(R.id.infoType)
        val textViewData: TextView = view.findViewById(R.id.data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_info_destino, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        Glide.with(holder.itemView.context)
            .load(item.icon)  // Aquí se carga la imagen usando el path
            .into(holder.imageView)

        holder.textViewInfoType.text = item.info
        holder.textViewData.text = item.data

    }

    override fun getItemCount(): Int = itemList.size

    // Método para actualizar los elementos del adaptador
    fun updateItems(newItems: MutableList<ItemInfo>) {
        itemList = newItems
        notifyDataSetChanged()
    }

    fun addItem(newItem: ItemInfo) {
        itemList.add(newItem)
        notifyItemInserted(0)
    }
}
