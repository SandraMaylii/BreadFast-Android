package com.upao.panaderia.api.apiEndpoints

import com.upao.panaderia.models.Products
import com.upao.panaderia.models.requestModel.PedidosRequest
import com.upao.panaderia.models.requestModel.RegisterRequest
import com.upao.panaderia.models.responseModel.ApiResponse
import com.upao.panaderia.models.responseModel.CategoryResponse
import com.upao.panaderia.models.responseModel.PedidoResponse
import com.upao.panaderia.models.responseModel.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("user/store")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<ApiResponse>

    @GET("user/{email}")
    suspend fun userGet(@Path("email") email: String): Response<UserResponse>

    @POST("vendor/store")
    suspend fun registerVendor(@Body registerRequest: RegisterRequest): Response<ApiResponse>

    @GET("category/get_products/{id}")
    suspend fun getProducts(@Path("id") id: Int): Response<List<Products>>

    @POST("order/place-order")
    suspend fun placeOrder(@Body pedidoRequest: PedidosRequest): Response<ApiResponse>

    @GET("order/{id}")
    suspend fun getOrder(@Path("id") id: Int): Response<PedidoResponse>
}