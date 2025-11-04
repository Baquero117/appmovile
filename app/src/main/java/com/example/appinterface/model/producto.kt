package com.example.appinterface.model

import com.google.gson.annotations.SerializedName

data class producto (
    var id_producto: Int,
    var nombre: String,
    var descripcion: String,
    var cantidad: Int,
    @SerializedName("imagen") var imagen: String,
    @SerializedName("id_vendedor") var id_vendedor: Int,
    var estado: String
)
