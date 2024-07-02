package com.upao.panaderia.service

import android.content.Context
import com.upao.panaderia.models.requestModel.RegisterRequest
import com.upao.panaderia.models.requestModel.UserRequest
import com.upao.panaderia.models.responseModel.UserResponse
import com.upao.panaderia.repositories.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserService(context: Context) {

    private val userRepository = UserRepository(context)

    fun login(email: String, password: String): Boolean {
        return true
    }

    fun register(context: Context, user: RegisterRequest, onResult: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val isSuccess = userRepository.register(context, user)
            withContext(Dispatchers.Main) {
                onResult(isSuccess)
            }
        }
    }

    fun getUser(context: Context, email: String, onResult: (UserResponse) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = userRepository.getUser(context, email)
            withContext(Dispatchers.Main) {
                onResult(user)
            }
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