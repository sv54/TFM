package com.example.tfm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CommentsAdapter(private var itemList: MutableList<ItemComment> = mutableListOf()) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    private var onLoadMoreListener: OnLoadMoreListener? = null

    fun setOnLoadMoreListener(listener: OnLoadMoreListener) {
        onLoadMoreListener = listener
    }

    var moreComments: Boolean = true
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val textUsername: TextView = view.findViewById(R.id.textUsername)
        val textExpenses: TextView = view.findViewById(R.id.textExpenses)
        val textDays: TextView = view.findViewById(R.id.textDays)
        val textComment: TextView = view.findViewById(R.id.textComment)
        val textRate: TextView = view.findViewById(R.id.textRate)
        override fun onClick(v: View?) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textComment.text = itemList[position].comment
        holder.textExpenses.text = itemList[position].expenses.toString()
        holder.textDays.text = itemList[position].days.toString()
        holder.textUsername.text = itemList[position].username
        holder.textRate.text = itemList[position].rate.toString()

        if (position === itemList.size - 1) {
            if(moreComments){
                onLoadMoreListener?.onLoadMore()
            }
            else{
                val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
                params.bottomMargin = 300 // last item bottom margin
                holder.itemView.layoutParams = params
            }

        }
        else{
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            params.bottomMargin = 4
            holder.itemView.layoutParams = params
        }
    }

    override fun getItemCount(): Int = itemList.size

    fun updateItems(newItems: MutableList<ItemComment>) {
        itemList = newItems
        notifyDataSetChanged()  // Notificar al RecyclerView que los datos han cambiado
    }

    fun addItems(newItemComment: MutableList<ItemComment>){
        itemList.addAll(newItemComment)
        notifyDataSetChanged()
    }
}