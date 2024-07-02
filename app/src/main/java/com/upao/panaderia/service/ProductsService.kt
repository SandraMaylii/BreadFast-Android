package com.upao.panaderia.service

import android.content.Context
import com.upao.panaderia.models.Products
import com.upao.panaderia.repositories.ProductsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsService(context: Context) {
    private val productsRepository = ProductsRepository(context)

    fun getProducts(context: Context, id: Int, onResult: (List<Products>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val products = productsRepository.getProducts(context, id)
            withContext(Dispatchers.Main) {
                onResult(products)
            }
        }
    }
}