package com.varcassoftware.ridercabapp.activity.login

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varcassoftware.ridercabapp.entity.LoginResponse
import com.varcassoftware.ridercabapp.entity.UserLogin
import com.varcassoftware.ridercabapp.repository.Repository
import com.varcassoftware.ridercabapp.response.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(val repository: Repository) : ViewModel() {
    val user = MutableLiveData<LoginResponse?>()
    var error = MutableLiveData<String>()
    var firstName = MutableLiveData<String>()
    var password = MutableLiveData<String>()



    val loginResult = MutableLiveData<Boolean>()


     fun validateCredentials() {
         loginResult.value  = isFirstNameValid(firstName.value?:"") && isPasswordValid(password.value?:"")

    }

     fun saveData(userLogin : UserLogin){
        viewModelScope.launch {
            val response = repository.saveUserLoginDetails(userLogin)
            when (response) {
                is ApiResponse.Success -> {
                    withContext(Dispatchers.Main) {
                        val data = response.data
                        if(data.status.equals("Success", ignoreCase = true)){
                            val loginResponse =  data.data;
                            if (loginResponse!=null) {
                                user.postValue(loginResponse)
                            }
                            else{
                                withContext(Dispatchers.Main) {
                                    error.value = "Something Went Wrong. Please Try Again"
                                }
                            }
                        }else{
                            withContext(Dispatchers.Main) {
                                error.value = "Something Went Wrong. Please Try Again"
                            }
                        }
                    }
                }
                is ApiResponse.Error -> {
                    withContext(Dispatchers.Main) {
                        error.value = response.message ?: response.exception.toString()
                                ?: "Something Went Wrong. Please Try Again"
                    }

                }
            }
        }
    }

    private fun isFirstNameValid(firstName: String): Boolean {
        return firstName.isNotEmpty()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 4
    }
}