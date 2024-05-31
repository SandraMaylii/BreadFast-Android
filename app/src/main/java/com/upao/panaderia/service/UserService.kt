package com.upao.panaderia.service

import android.content.Context
import com.upao.panaderia.models.UserRequest
import com.upao.panaderia.models.UserResponse
import com.upao.panaderia.repositories.UserRepository

class UserService(context: Context) {

    private val userRepository = UserRepository(context)

    fun login(email: String, password: String): Boolean {
        return true
    }

    fun register(user: UserRequest): Boolean {
        userRepository.open()
        val result = userRepository.findByEmail(user.email)
        return if (result) {
            userRepository.close()
            false
        } else {
            val response = userRepository.register(user)
            userRepository.close()
            response
        }
    }


    fun update(user: UserRequest): Boolean {
        return true
    }

    fun delete(email: String): Boolean {
        return true
    }

    fun logout(): Boolean {
        return true
    }

    companion object {
        val LOGTAG = "DB-> "
    }
}