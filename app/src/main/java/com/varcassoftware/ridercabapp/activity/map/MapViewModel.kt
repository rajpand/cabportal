package com.varcassoftware.ridercabapp.activity.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel(val forWhat:String) : ViewModel() {
    var _initValues = MutableLiveData<Boolean>()
    val initValues :LiveData<Boolean> = _initValues
    init {
       // _initValues.value = true
        _initValues.postValue(true)
    }
}