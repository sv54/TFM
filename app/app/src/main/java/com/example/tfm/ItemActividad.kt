package com.example.tfm

data class ItemActividad(
    var id: Int = -1,
    var titulo: String = "none",
    var descripcion: String = "none",
    var numRecomendado: Int = -1,
    var imagenes: MutableList<String> = mutableListOf()
)
