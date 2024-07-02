package com.upao.panaderia.repositories

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.upao.panaderia.api.apiClient.Apiclient
import com.upao.panaderia.api.apiEndpoints.ApiService
import com.upao.panaderia.models.ApiError
import com.upao.panaderia.models.requestModel.RegisterRequest
import com.upao.panaderia.models.responseModel.ErrorResponse
import com.upao.panaderia.models.responseModel.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

class UserRepository(context: Context) {

    suspend fun register(context: Context, user: RegisterRequest) : Boolean{
        val apiService = Apiclient.createService(ApiService::class.java)
        val response = apiService.register(user)
        return withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                val registerResponse = response.body()
                Toast.makeText(context, registerResponse?.msg, Toast.LENGTH_SHORT).show()
                true
            } else {
                val errorResponse = response.errorBody()?.string()
                val apiErrors = parseError(errorResponse)
                apiErrors?.let { errors ->
                    for (error in errors) {
                        val capitalizedCode = error.code.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        }
                        Toast.makeText(
                            context,
                            "${capitalizedCode}: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } ?: run {
                    Toast.makeText(context, "Error desconocido", Toast.LENGTH_SHORT).show()
                }
                false
            }
        }
    }

    suspend fun getUser(context: Context, email: String): UserResponse {
        val apiService = Apiclient.createService(ApiService::class.java)
        val response = apiService.userGet(email)
        return withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                val data = response.body()
                if(data != null) {
                    val user = UserResponse(
                        id=data.id,
                        nombre=data.nombre,
                        apellido= data.apellido,
                        email=data.email,
                        ptsFidelidad=data.ptsFidelidad,
                        createdAt=data.createdAt,
                        updatedAt=data.updatedAt
                    )
                    user
                } else {
                    UserResponse(0,"","","",0,"","")
                }
            } else {
                val errorResponse = response.errorBody()?.string()
                val apiErrors = parseError(errorResponse)
                apiErrors?.let { errors ->
                    for (error in errors) {
                        val capitalizedCode = error.code.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        }
                        Toast.makeText(
                            context,
                            "${capitalizedCode}: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } ?: run {
                    Toast.makeText(context, "Error desconocido", Toast.LENGTH_SHORT).show()
                }
                UserResponse(0,"","","",0,"","")
            }
        }
    }

    private fun parseError(errorBody: String?): List<ApiError>? {
        return try {
            errorBody?.let {
                val gson = Gson()
                val errorResponse = gson.fromJson(it, ErrorResponse::class.java)
                errorResponse.errors
            }
        } catch (e: Exception) {
            null
        }
    }
}