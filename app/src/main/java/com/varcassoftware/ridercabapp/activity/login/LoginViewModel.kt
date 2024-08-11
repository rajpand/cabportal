package com.varcassoftware.ridercabapp.activity.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.varcassoftware.ridercabapp.entity.UserLogin
import com.varcassoftware.ridercabapp.repository.Repository
import com.varcassoftware.ridercabapp.response.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(val repository: Repository) : ViewModel() {
    val user = MutableLiveData<UserLogin>()
    var error = MutableLiveData<String>()
    val loginResult = MutableLiveData<Boolean>()


     fun validateCredentials() {
        val userLogin = user.value ?: UserLogin("", "")
        if (isFirstNameValid(userLogin.firstName) && isPasswordValid(userLogin.password)) {
            loginResult.value = true
        } else {
            loginResult.value = false
        }
    }
    suspend fun saveData(){
        user.value?.let {
            val response = repository.saveUserLoginDetails(it)
            when (response) {
                is ApiResponse.Success -> {
                    withContext(Dispatchers.Main) {
                        user.value = response.data
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
        return password.length >= 6
    }
}