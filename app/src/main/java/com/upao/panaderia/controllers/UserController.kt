package com.upao.panaderia.controllers

import android.content.Context
import com.upao.panaderia.models.UserRequest
import com.upao.panaderia.service.UserService

class UserController (context: Context){
    private val userService = UserService(context)

    // Login method
    fun login(email: String, password: String): Boolean {
        return true
    }

    // Register method
    fun register(user: UserRequest): Boolean {
        return userService.register(user)
    }

    // Update method
    fun update(user: UserRequest): Boolean {
        return true
    }

    // Delete method
    fun delete(user: UserRequest): Boolean {
        return true
    }
}