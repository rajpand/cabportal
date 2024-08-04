package com.varcassoftware.ridercabapp.activity.welcomeActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



class WelcomeViewModel(private val application: Application, private val someParameter: String) : AndroidViewModel(application) {
    var liveData = MutableLiveData<Boolean>()

    fun init() {
        viewModelScope.launch {
            delay(5000)
            updateLiveData()
        }
    }

    private fun updateLiveData() {
        liveData.postValue(true)
    }
}