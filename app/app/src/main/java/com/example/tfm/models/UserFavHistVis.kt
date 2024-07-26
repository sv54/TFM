package com.example.tfm.models

data class Favorite(val usuarioId: Int, val destinoId: Int)
data class Visited(val usuarioId: Int, val destinoId: Int, val fechaVisita: Long)
data class History(val usuarioId: Int, val destinoId: Int, val fechaEntrado: Long)
data class Recommended(val usuarioId: Int, val actividadId: Int)
