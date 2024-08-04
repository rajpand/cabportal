package com.varcassoftware.ridercabapp.viewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varcassoftware.ridercabapp.activity.loginActivity.UserAccountViewModel
import com.varcassoftware.ridercabapp.activity.map.MapViewModel
import com.varcassoftware.ridercabapp.activity.splashActivity.SplashViewModel

class ViewModelFactory(private val someParameter: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SplashViewModel(someParameter) as T
        }
        if (modelClass.isAssignableFrom(UserAccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserAccountViewModel(someParameter) as T
        }
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapViewModel(someParameter) as T
        }



        throw IllegalArgumentException("Unknown ViewModel class")
    }
}