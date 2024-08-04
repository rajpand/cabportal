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
            ServiceItem("Service 1", "Description for service 1", R.drawable.ic_car_cab),
            ServiceItem("Service 2", "Description for service 2", R.drawable.ic_car),
            ServiceItem("Service 3", "Description for service 3", R.drawable.ic_bike),
            ServiceItem("Service 4", "Description for service 4", R.drawable.ic_car_cab),
            ServiceItem("Service 5", "Description for service 3", R.drawable.ic_bike),
            ServiceItem("Service 6", "Description for service 4", R.drawable.ic_car_cab),
            ServiceItem("Service 7", "Description for service 4", R.drawable.ic_car_cab),
            ServiceItem("Service 8", "Description for service 3", R.drawable.ic_bike),
            ServiceItem("Service 9", "Description for service 4", R.drawable.ic_car_cab),
            ServiceItem("Service 7", "Description for service 4", R.drawable.ic_car_cab),
            ServiceItem("Service 8", "Description for service 3", R.drawable.ic_bike),
            ServiceItem("Service 9", "Description for service 4", R.drawable.ic_car_cab),
        )
    }
    val serviceList: LiveData<List<ServiceItem>> = _serviceList
}
