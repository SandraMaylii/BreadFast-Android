package com.upao.panaderia.models.adaptersModel

class Producto {

    var nombre: String = ""
    var precio: Double = 0.0
    var imagen: String = ""

    constructor()
    constructor(nombre: String, precio: Double, imagen: String) {
        this.nombre = nombre
        this.precio = precio
        this.imagen = imagen
    }
}