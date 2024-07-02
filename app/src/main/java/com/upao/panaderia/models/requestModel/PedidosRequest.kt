package com.upao.panaderia.models.requestModel

data class PedidosRequest (
    val total: Double,
    val estado: String,
    val email: String
){
}