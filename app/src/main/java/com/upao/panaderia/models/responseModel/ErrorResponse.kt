package com.upao.panaderia.models.responseModel

import com.upao.panaderia.models.ApiError

data class ErrorResponse(
    val errors: List<ApiError>
)