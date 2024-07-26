package com.example.tfm

import com.example.tfm.models.ItemDestinoVisitado
import com.example.tfm.models.ItemListaDestino

interface OnItemClickListener {
    fun onItemClick(item: ItemListaDestino)
}

interface OnItemVisitedClickListener {
    fun onItemClick(item: ItemDestinoVisitado)
}

interface FragmentChangeListener {
    fun onFragmentChange(id: Int, titulo: String)
}

interface ApiListener{
    fun onEventCompleted()
    fun onEventFailed()
}