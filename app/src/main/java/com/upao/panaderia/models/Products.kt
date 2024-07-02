package com.upao.panaderia.models

data class Products(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val imagen: String,
    val categoria_id: Int,
    val created_at: String,
    val updated_at: String){
}