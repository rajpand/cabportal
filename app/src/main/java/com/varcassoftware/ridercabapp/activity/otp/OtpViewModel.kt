package com.varcassoftware.ridercabapp.activity.otp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.varcassoftware.ridercabapp.repository.Repository

class OtpViewModel(val repository: Repository) : ViewModel() {
    var forLogin = MutableLiveData<Boolean>();
    private var _otp = MutableLiveData<String>()
    var otpVerified = MutableLiveData<Boolean>()

    fun updateOtp(otp: String) {
        _otp.value = otp
    }

    fun verifyOtp() {
        if (_otp.value == "1234") {
            otpVerified.postValue(true)
        } else {
            otpVerified.postValue(false)
        }
    }

    fun clearData() {
        _otp.value = ""
    }
}