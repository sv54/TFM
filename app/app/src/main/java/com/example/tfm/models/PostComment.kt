package com.example.tfm.models

data class PostComment(
    var usuarioId: Int,
    var destinoId: Int,
    var texto: String,
    var permisoExtraInfo: Boolean,
    var estanciaDias: Int,
    var dineroGastado: Int,
    var valoracion: Int,
)
