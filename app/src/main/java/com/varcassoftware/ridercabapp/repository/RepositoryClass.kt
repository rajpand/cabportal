package com.varcassoftware.ridercabapp.repository

import com.varcassoftware.ridercabapp.networkResponse.RetrofitBuilder
import com.varcassoftware.ridercabapp.response.ApiResponse
import com.varcassoftware.ridercabapp.response.ResponseCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope

class RepositoryClass {

    /*fun fetchUsers() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitBuilder.apiService.getUsers()
                if (response.isSuccessful) {
                    _usersLiveData.postValue(ApiResponse.Success(response.body() ?: emptyList()))
                } else {
                    _usersLiveData.postValue(ApiResponse.Error("API Error: ${ ResponseCode.getResponseErrorMessageAccordingToCode(response.code())}"))
                }
            } catch (e: Exception) {
                _usersLiveData.postValue(ApiResponse.Error(exception = e))

            }
        }
    }*/

}