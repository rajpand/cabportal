package com.varcassoftware.ridercabapp.repository

import com.example.app.model.CustomerRegistration
import com.varcassoftware.ridercabapp.entity.UserLogin
import com.varcassoftware.ridercabapp.response.ApiResponse
import retrofit2.Call

interface Repository {
    suspend fun createCustomerRegistration(customer: CustomerRegistration): ApiResponse<String>
    suspend fun saveUserLoginDetails(userLogin: UserLogin):ApiResponse<UserLogin>
}