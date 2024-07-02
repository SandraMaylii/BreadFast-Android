package com.upao.panaderia.models.responseModel

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("msg") val msg: String
) {
}