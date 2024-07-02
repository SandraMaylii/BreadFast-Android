package com.upao.panaderia.helpers

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {

    private const val PREFS_NAME = "app_prefs"
    private const val USER_KEY = "user_key"
    private const val TOKEN = "token"
    private const val CART_PRODUCTS = "cart_products"
    private const val PREF_NAME = "PanaderiaPreferences"
    private const val KEY_CART_PRODUCTS = "cart_products"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setUserData(context: Context, user: String) {
        val editor = getPreferences(context).edit()
        editor.putString(USER_KEY, user)
        editor.apply()
    }

    fun getUserData(context: Context): String? {
        return getPreferences(context).getString(USER_KEY, null)
    }

    fun removeUserData(context: Context) {
        val editor = getPreferences(context).edit()
        editor.remove(USER_KEY)
        editor.apply()
    }

    fun setToken(context: Context, token: String) {
        val editor = getPreferences(context).edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun getToken(context: Context): String? {
        return getPreferences(context).getString(TOKEN, null)
    }


    private fun getProducts(context: Context): MutableSet<String>? {
        return getPreferences(context).getStringSet(CART_PRODUCTS, mutableSetOf())
    }

    //fun getCartProducts(context: Context): MutableSet<String>? {
    //   return getProducts(context)
    // }

    //fun removeCartProducts(context: Context) {
    //  val editor = getPreferences(context).edit()
    // editor.remove(CART_PRODUCTS)
    // editor.apply()
    //}
    fun saveCartProducts(context: Context, products: List<String>) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        val productSet = products.toSet()
        editor.putStringSet(KEY_CART_PRODUCTS, productSet)
        editor.apply()
    }

    fun getCartProducts(context: Context): List<String>? {
        val prefs = getPreferences(context)
        val productSet = prefs.getStringSet(KEY_CART_PRODUCTS, null)
        return productSet?.toList()
    }

    fun saveProduct(context: Context, product: String) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        val productSet = prefs.getStringSet(KEY_CART_PRODUCTS, mutableSetOf())?.toMutableSet()
        productSet?.add(product)
        editor.putStringSet(KEY_CART_PRODUCTS, productSet)
        editor.apply()
    }

    fun updateProduct(context: Context, updatedProduct: String) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        val productSet = prefs.getStringSet(KEY_CART_PRODUCTS, mutableSetOf())?.toMutableSet()
        val productData = updatedProduct.split(",")
        val title = productData[0]
        val description = productData[1]

        // Encontrar y actualizar el producto existente
        val existingProduct = productSet?.find {
            val data = it.split(",")
            data[0] == title && data[1] == description
        }
        existingProduct?.let {
            productSet.remove(it)
        }
        productSet?.add(updatedProduct)
        editor.putStringSet(KEY_CART_PRODUCTS, productSet)
        editor.apply()
    }

    fun removeCartProducts(context: Context) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        editor.remove(KEY_CART_PRODUCTS)
        editor.apply()
    }
}