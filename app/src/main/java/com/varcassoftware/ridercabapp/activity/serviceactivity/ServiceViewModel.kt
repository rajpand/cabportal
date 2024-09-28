package com.varcassoftware.ridercabapp.activity.serviceactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.entity.ServiceItem

class ServiceViewModel : ViewModel() {

    private val _imageList = MutableLiveData<List<Int>>().apply {
        value = listOf(
            R.drawable.services_slider_1,
            R.drawable.services_slider_2,
            R.drawable.services_slider_3,
            R.drawable.services_slider_4
        )
    }
    val imageList: LiveData<List<Int>> = _imageList

    private val _serviceList = MutableLiveData<List<ServiceItem>>().apply {
        value = listOf(
            ServiceItem("Passenger bike", "Description for service 1", R.drawable.ic_bike),
            ServiceItem("Passenger Car", "Description for service 2", R.drawable.ic_car),
            ServiceItem("Booking bike", "Description for service 3", R.drawable.ic_bike),
            ServiceItem("Booking Car", "Description for service 4", R.drawable.ic_car),
            ServiceItem("Rental Bike", "Description for service 3", R.drawable.ic_bike),
            ServiceItem("Rental Car", "Description for service 4", R.drawable.ic_car_cab),
        )
    }
    val serviceList: LiveData<List<ServiceItem>> = _serviceList
}
