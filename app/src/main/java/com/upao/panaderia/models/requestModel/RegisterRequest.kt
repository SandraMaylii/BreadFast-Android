package com.upao.panaderia.models.requestModel

data class RegisterRequest (
    val nombres: String,
    val apellidos: String,
    val email: String,
    val password: String,
){
}