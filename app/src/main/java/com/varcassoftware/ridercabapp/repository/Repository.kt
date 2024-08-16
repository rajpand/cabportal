package com.varcassoftware.ridercabapp.repository

import com.example.app.model.CustomerRegistration
import com.varcassoftware.ridercabapp.entity.UserLogin
import com.varcassoftware.ridercabapp.response.ApiResponse
import com.varcassoftware.ridercabapp.response.ResponseStatusApi

interface Repository {
    suspend fun createCustomerRegistration(customer: CustomerRegistration): ApiResponse<ResponseStatusApi>
    suspend fun saveUserLoginDetails(userLogin: UserLogin):ApiResponse<UserLogin>
}