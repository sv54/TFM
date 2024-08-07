package com.example.tfm.models

data class Reporte(
    private var usuarioId: Int = -1,
    private var timestamp: Long,
    private var titulo: String = "",
    private var textoReporte: String = ""
)
