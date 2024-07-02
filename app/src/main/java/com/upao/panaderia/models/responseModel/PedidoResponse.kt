package com.upao.panaderia.models.responseModel

import com.google.gson.annotations.SerializedName

data class PedidoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("total") val total: Double,
    @SerializedName("estado") val estado: String,
    @SerializedName("usuario_id") val usuario_id: Int,
    @SerializedName("direccion") val direccion: String,
    @SerializedName("qrCode") val qrCode: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String
) {
}