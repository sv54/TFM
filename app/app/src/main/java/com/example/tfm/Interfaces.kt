package com.example.tfm

import com.example.tfm.models.ItemListaDestino

interface OnItemClickListener {
    fun onItemClick(item: ItemListaDestino)
}

interface FragmentChangeListener {
    fun onFragmentChange(id: Int, titulo: String)
}

interface ApiListener{
    fun onEventCompleted()
    fun onEventFailed()
}