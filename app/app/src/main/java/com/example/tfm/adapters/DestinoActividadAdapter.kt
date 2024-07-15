package com.example.tfm.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfm.models.ItemActividad
import com.example.tfm.R
import com.google.android.material.imageview.ShapeableImageView

class DestinoActividadAdapter(
    private var listaActividades: List<ItemActividad>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<DestinoActividadAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val previewImage: ShapeableImageView = itemView.findViewById(R.id.previewImage)
        val actividadTitulo: TextView = itemView.findViewById(R.id.infoType)
        val numRecomendado: TextView = itemView.findViewById(R.id.data)
        val buttonRecomendar: ImageButton = itemView.findViewById(R.id.buttonRecomendar)

        init {
            itemView.setOnClickListener(this)
            buttonRecomendar.setOnClickListener {
                listener.onItemClick(listaActividades[adapterPosition])
            }
        }

        override fun onClick(v: View?) {
            listener.onItemClick(listaActividades[adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actividad, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val actividad = listaActividades[position]

        holder.actividadTitulo.text = actividad.titulo
        holder.numRecomendado.text = actividad.numRecomendado.toString()

        Glide.with(holder.itemView.context)
            .load(actividad.imagenes.firstOrNull())
            .into(holder.previewImage)
    }

    override fun getItemCount(): Int {
        return listaActividades.size
    }

    fun updateItems(newItems: MutableList<ItemActividad>) {
        listaActividades = newItems
        Log.i("tagg", listaActividades.toString())

        notifyDataSetChanged()  // Notificar al RecyclerView que los datos han cambiado
    }

    interface OnItemClickListener {
        fun onItemClick(item: ItemActividad)
    }

}
