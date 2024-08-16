package com.varcassoftware.ridercabapp.repository

import com.example.app.model.CustomerRegistration
import com.varcassoftware.ridercabapp.entity.UserLogin
import com.varcassoftware.ridercabapp.networkResponse.RetrofitBuilder
import com.varcassoftware.ridercabapp.networkResponse.RetrofitService
import com.varcassoftware.ridercabapp.response.ApiResponse
import com.varcassoftware.ridercabapp.response.ResponseCode
import com.varcassoftware.ridercabapp.response.ResponseStatusApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryClass : Repository {
    private val apiService: RetrofitService = RetrofitBuilder.apiService

    override suspend fun createCustomerRegistration(customer: CustomerRegistration):
            ApiResponse<ResponseStatusApi> {

        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCustomerRegistration(customer)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        ApiResponse.Success(responseBody)
                    } else {
                        ApiResponse.Error("Received null response body")
                    }
                } else {
                    ApiResponse.Error("API Error: ${ResponseCode.getResponseErrorMessageAccordingToCode(response.code())}")
                }
            } catch (e: Exception) {
                 ApiResponse.Error(exception = e)
            }
        }
    }

    override suspend fun saveUserLoginDetails(userLogin: UserLogin): ApiResponse<UserLogin> {
        return withContext(Dispatchers.IO) {
            try {
                val response =  apiService.saveUserLoginDetails(userLogin)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        ApiResponse.Success(responseBody)
                    } else {
                        ApiResponse.Error("Received null response body")
                    }
                } else {
                    ApiResponse.Error("API Error: ${ResponseCode.getResponseErrorMessageAccordingToCode(response.code())}")
                }
            } catch (e: Exception) {
                ApiResponse.Error(exception = e)
                ApiResponse.Error("API Error: ${e.toString()}")

            }
        }
    }
}
