package com.upao.panaderia.controllers

import android.content.Context
import com.upao.panaderia.models.Products
import com.upao.panaderia.service.ProductsService

class ProductsController(context: Context) {
    private val productsService = ProductsService(context)

    fun getProducts(context: Context, id: Int, onResult: (List<Products>) -> Unit) {
        productsService.getProducts(context, id) { products ->
            onResult(products)
        }
    }
}