package com.example.tfm

data class ItemDestino (
    var id: Int = -1,
    var titulo: String = "none",
    var descripcion: String = "none",
    var puntuaciones: Float = -1.0f,
    var gastoDia: Int = -1,
    var indiceSeguridad: Int = -1,
    var moneda: String = "none",
    var clima: String = "none",
    var visitas: Int = -1,
    var pais: String = "none",
    var imagenes: MutableList<String> = mutableListOf()
)



/*
id: row.id,
titulo: row.titulo,
descripcion: row.descripcion,
-paisId: row.paisId,
-numPuntuaciones: row.numPuntuaciones,
-sumaPuntuaciones: row.sumaPuntuaciones,
-gastoTotal: row.gastoTotal,
-diasEstanciaTotal: row.diasEstanciaTotal,
indiceSeguridad: row.indiceSeguridad,
moneda: row.moneda,
clima: row.clima,
numVisitas: row.numVisitas,
nombrePais: row.nombrePais,
imagenes: imagenes
 */