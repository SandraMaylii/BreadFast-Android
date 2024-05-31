package com.upao.panaderia.models

class UserRequest {
    var nombre: String = ""
    var apellido: String = ""
    var email: String = ""
    var password: String = ""
    var rol: String = ""
    var isActive: Int = 0
    var isDelete: Int = 0
    var createdAT: String = ""
    var updatedAT: String = ""
    constructor()
    constructor(
        nombre: String,
        apellido: String,
        email: String,
        password: String,
        rol: String,
        isActive: Int,
        isDelete: Int,
        createdAT: String,
        updatedAT: String
    ) {
        this.nombre = nombre
        this.apellido = apellido
        this.email = email
        this.password = password
        this.rol = rol
        this.isActive = isActive
        this.isDelete = isDelete
        this.createdAT = createdAT
        this.updatedAT = updatedAT
    }

    override fun toString(): String {
        return "UserRequest(nombre='$nombre', apellido='$apellido', email='$email', password='$password', rol='$rol', isActive=$isActive, isDelete=$isDelete, createdAT='$createdAT', updatedAT='$updatedAT')"
    }
}