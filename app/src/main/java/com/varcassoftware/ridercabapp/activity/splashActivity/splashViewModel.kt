package com.varcassoftware.ridercabapp.activity.splashActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varcassoftware.ridercabapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(private val someParameter: String) : ViewModel() {
    var liveData = MutableLiveData<Boolean>()

    fun init() {
       viewModelScope.launch {
           delay(3000)
           updateLiveData()
       }
    }

    private fun updateLiveData() {
      liveData.postValue(true)
    }
}