package com.example.tfm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.models.ItemComment
import com.example.tfm.R

class CommentsAdapter(private var itemList: MutableList<ItemComment> = mutableListOf()) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    private var onLoadMoreListener: OnLoadMoreListener? = null

    fun setOnLoadMoreListener(listener: OnLoadMoreListener) {
        onLoadMoreListener = listener
    }

    var moreComments: Boolean = true

    companion object {
        const val TYPE_SIMPLE = 0
        const val TYPE_DETAILED = 1
    }

    open inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val textUsername: TextView = view.findViewById(R.id.textUsername)
        val textComment: TextView = view.findViewById(R.id.textComment)
        val textRate: TextView = view.findViewById(R.id.textRate)
        override fun onClick(v: View?) {
        }
    }

    inner class SimpleViewHolder(view: View) : ViewHolder(view)

    inner class DetailedViewHolder(view: View) : ViewHolder(view) {
        val textExpenses: TextView = view.findViewById(R.id.textExpenses)
        val textDays: TextView = view.findViewById(R.id.textDays)
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].permissionExtra == 0) TYPE_SIMPLE else TYPE_DETAILED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_SIMPLE) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_comment_simple, parent, false)
            SimpleViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_comment, parent, false)
            DetailedViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]

        when (holder.itemViewType) {
            TYPE_SIMPLE -> {
                val simpleHolder = holder as SimpleViewHolder
                simpleHolder.textComment.text = item.comment
                simpleHolder.textUsername.text = item.username
                simpleHolder.textRate.text = item.rate.toString()
            }
            TYPE_DETAILED -> {
                val detailedHolder = holder as DetailedViewHolder
                detailedHolder.textComment.text = item.comment
                detailedHolder.textExpenses.text = item.expenses.toString()
                detailedHolder.textDays.text = item.days.toString()
                detailedHolder.textUsername.text = item.username
                detailedHolder.textRate.text = item.rate.toString()
            }
        }

        if (position == itemList.size - 1) {
            if(moreComments){
                onLoadMoreListener?.onLoadMore()
            } else {
                val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
                params.bottomMargin = 300 // last item bottom margin
                holder.itemView.layoutParams = params
            }
        } else {
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
