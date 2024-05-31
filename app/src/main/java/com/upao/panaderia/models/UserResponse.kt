package com.upao.panaderia.models

class UserResponse {
    var nombre: String = ""
    var apellido: String = ""
    var email: String = ""
    var rol: String = ""

    constructor()

    constructor(
        nombre: String,
        apellido: String,
        email: String,
        rol: String
    ) {
        this.nombre = nombre
        this.apellido = apellido
        this.email = email
        this.rol = rol
    }

    override fun toString(): String {
        return "UserResponse(nombre='$nombre', apellido='$apellido', email='$email', rol='$rol')"
    }

}