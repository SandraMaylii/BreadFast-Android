package com.upao.panaderia.models.responseModel

import com.google.gson.annotations.SerializedName

class UserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nombres") val nombre: String,
    @SerializedName("apellidos") val apellido: String,
    @SerializedName("email") val email: String,
    @SerializedName("puntosFidelidad") val ptsFidelidad: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
) {
    override fun toString(): String {
        return "UserResponse(id=$id, nombre='$nombre', apellido='$apellido', email='$email', ptsFidelidad=$ptsFidelidad, createdAt='$createdAt', updatedAt='$updatedAt')"
    }
}