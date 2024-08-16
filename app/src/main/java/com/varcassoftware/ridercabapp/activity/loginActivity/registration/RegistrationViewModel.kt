package com.varcassoftware.ridercabapp.activity.loginActivity.registration

import SingleLiveEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.model.CustomerRegistration
import com.varcassoftware.ridercabapp.repository.Repository
import com.varcassoftware.ridercabapp.response.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationViewModel(private val repository: Repository) : ViewModel() {

    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val mobileNumber = MutableLiveData<String>()
    val aadharNumber = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _isFormValid = MutableLiveData<Boolean>()
    val isFormValid: LiveData<Boolean> get() = _isFormValid

    private val _nameError = MutableLiveData<String?>()
    val nameError: LiveData<String?> get() = _nameError

    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> get() = _emailError

    private val _mobileNumberError = MutableLiveData<String?>()
    val mobileNumberError: LiveData<String?> get() = _mobileNumberError

    private val _aadharNumberError = MutableLiveData<String?>()
    val aadharNumberError: LiveData<String?> get() = _aadharNumberError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> get() = _passwordError

    private val _customerCreated = SingleLiveEvent<String?>()
    val customerCreated: LiveData<String?> get() = _customerCreated

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+$"
        return email.matches(Regex(emailPattern))
    }

    private fun isValidMobileNumber(mobileNumber: String): Boolean {
        val mobilePattern = "^[0-9]{10}$"
        return mobileNumber.matches(Regex(mobilePattern))
    }

    fun validateForm() {
        var valid = true

        if (name.value.isNullOrEmpty()) {
            _nameError.value = "Enter Name"
            valid = false
        } else {
            _nameError.value = null
        }

        if (email.value.isNullOrEmpty() || !isValidEmail(email.value!!)) {
            _emailError.value = "Invalid email address"
            valid = false
        } else {
            _emailError.value = null
        }

        if (mobileNumber.value.isNullOrEmpty() || !isValidMobileNumber(mobileNumber.value!!)) {
            _mobileNumberError.value = "Invalid mobile number"
            valid = false
        } else {
            _mobileNumberError.value = null
        }

        if (aadharNumber.value.isNullOrEmpty()) {
            _aadharNumberError.value = "Invalid Aadhaar number"
            valid = false
        } else {
            _aadharNumberError.value = null
        }

        if (password.value.isNullOrEmpty()) {
            _passwordError.value = "Enter Password"
            valid = false
        } else {
            _passwordError.value = null
        }

        _isFormValid.value = valid
    }

    fun createCustomer(deviceId: String) {
        viewModelScope.launch {
            val customer = CustomerRegistration(
                firstName = name.value ?: "",
                email = email.value ?: "",
                mobile = mobileNumber.value ?: "",
                aadhaar = aadharNumber.value ?: "",
                password = password.value ?: ""
            )
            val response = repository.createCustomerRegistration(customer)
            when (response) {
                is ApiResponse.Success -> {
                    val responseData = response.data
                    if(responseData.message?.contains("success",ignoreCase = true) == true){
                        withContext(Dispatchers.Main) {
                            _customerCreated.value = "success"
                        }
                    }else{
                        withContext(Dispatchers.Main) {
                            _customerCreated.value = "${responseData.message} Something Went Wrong. Please Try Again"
                        }
                    }
                }
                is ApiResponse.Error -> {
                    withContext(Dispatchers.Main) {
                        _customerCreated.value =
                            response.message ?: response.exception.toString()
                                    ?: "Something Went Wrong. Please Try Again"
                    }
                }
            }
        }
    }

    fun resetCustomerCreated() {
        _customerCreated.value = null
        _isFormValid.value = false
        clearData()
    }

    fun clearData() {
        name.value = ""
        email.value = ""
        mobileNumber.value = ""
        aadharNumber.value = ""
        password.value = ""
        _nameError.value = null
        _emailError.value = null
        _mobileNumberError.value = null
        _aadharNumberError.value = null
        _passwordError.value = null
        _isFormValid.value = false
        _customerCreated.value = null
    }
}
