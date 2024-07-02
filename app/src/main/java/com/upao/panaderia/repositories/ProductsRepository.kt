package com.upao.panaderia.repositories

import android.content.Context
import com.upao.panaderia.api.apiClient.Apiclient
import com.upao.panaderia.api.apiEndpoints.ApiService
import com.upao.panaderia.models.Products
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductsRepository(context: Context) {

    suspend fun getProducts(context: Context, id: Int): List<Products> {
        val apiService = Apiclient.createService(ApiService::class.java)
        val response = apiService.getProducts(id)
        return withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                response.body()!!
            } else {
                emptyList()
            }
        }
    }
}