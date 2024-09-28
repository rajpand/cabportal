package com.varcassoftware.ridercabapp.viewModelFactory


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varcassoftware.ridercabapp.activity.createpin.CreatePinViewModel
import com.varcassoftware.ridercabapp.activity.login.LoginViewModel
import com.varcassoftware.ridercabapp.activity.loginActivity.UserAccountViewModel
import com.varcassoftware.ridercabapp.activity.loginActivity.registration.RegistrationViewModel
import com.varcassoftware.ridercabapp.activity.map.MapViewModel
import com.varcassoftware.ridercabapp.activity.otp.OtpViewModel
import com.varcassoftware.ridercabapp.activity.splashActivity.SplashViewModel
import com.varcassoftware.ridercabapp.customers.models.BookingViewModel
import com.varcassoftware.ridercabapp.customers.viewmodels.TravelingServicesViewModels
import com.varcassoftware.ridercabapp.repository.Repository

class ViewModelFactory(private val someParameter: String="", private val repository: Repository) :
    ViewModelProvider.Factory {

    constructor(repository: Repository) : this("", repository)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return SplashViewModel(someParameter) as T
        }
        if (modelClass.isAssignableFrom(UserAccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return UserAccountViewModel(someParameter) as T
        }
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return MapViewModel(someParameter) as T
        }

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return LoginViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(OtpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return OtpViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return RegistrationViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(CreatePinViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return CreatePinViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(TravelingServicesViewModels::class.java)) {
            @Suppress("UNCHECKED_CAST") return TravelingServicesViewModels(repository) as T
        }

        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return BookingViewModel(repository) as T
        }



        throw IllegalArgumentException("Unknown ViewModel class")
    }
}