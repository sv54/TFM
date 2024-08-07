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

    companion object {
        private const val VIEW_TYPE_SIMPLE = 0
        private const val VIEW_TYPE_FULL = 1
    }

    inner class ViewHolder(view: View, viewType: Int) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.icInfo)
        val textViewInfoType: TextView = view.findViewById(R.id.infoType)
        val textViewData: TextView? = if (viewType == VIEW_TYPE_FULL) view.findViewById(R.id.data) else null
    }

    override fun getItemViewType(position: Int): Int {
        // Aquí puedes definir la lógica para decidir el tipo de vista
        return if (itemList[position].data.isEmpty()) VIEW_TYPE_SIMPLE else VIEW_TYPE_FULL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutId = if (viewType == VIEW_TYPE_FULL) R.layout.item_info_destino else R.layout.item_info_destino_simple
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)
        return ViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        Glide.with(holder.itemView.context)
            .load(item.icon)  // Aquí se carga la imagen usando el path
            .into(holder.imageView)

        holder.textViewInfoType.text = item.info
        holder.textViewData?.text = item.data
    }

    override fun getItemCount(): Int = itemList.size

    // Método para actualizar los elementos del adaptador
    fun updateItems(newItems: MutableList<ItemInfo>) {
        itemList = newItems
        notifyDataSetChanged()
    }

    fun addItem(newItem: ItemInfo) {
        itemList.add(newItem)
        notifyItemInserted(itemList.size - 1) // Cambiado para agregar al final de la lista
    }
}