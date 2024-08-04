package com.varcassoftware.ridercabapp.response

sealed class ApiResponse<out T : Any> {
    data class Success<out T : Any>(val data: T) : ApiResponse<T>()
    data class Error(val message: String? = null, val exception: Exception? = null) : ApiResponse<Nothing>()
}