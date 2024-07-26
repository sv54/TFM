package com.example.tfm.models

import android.os.Parcel
import android.os.Parcelable

data class ItemActividad(
    var id: Int = -1,
    var titulo: String = "none",
    var descripcion: String = "none",
    var numRecomendado: Int = -1,
    var imagenes: MutableList<String> = mutableListOf(),
    var recomendado: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "none",
        parcel.readString() ?: "none",
        parcel.readInt(),
        parcel.createStringArrayList() ?: mutableListOf(),
        parcel.readByte() != 0.toByte()  // Read the boolean value
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(titulo)
        parcel.writeString(descripcion)
        parcel.writeInt(numRecomendado)
        parcel.writeStringList(imagenes)
        parcel.writeByte(if (recomendado) 1 else 0)  // Write the boolean value
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItemActividad> {
        override fun createFromParcel(parcel: Parcel): ItemActividad {
            return ItemActividad(parcel)
        }

        override fun newArray(size: Int): Array<ItemActividad?> {
            return arrayOfNulls(size)
        }
    }
}
