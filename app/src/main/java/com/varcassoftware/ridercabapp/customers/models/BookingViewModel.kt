package com.varcassoftware.ridercabapp.customers.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.varcassoftware.ridercabapp.customers.data.Bus
import com.varcassoftware.ridercabapp.customers.data.Car
import com.varcassoftware.ridercabapp.customers.data.Innova
import com.varcassoftware.ridercabapp.customers.data.Vehicle
import com.varcassoftware.ridercabapp.repository.Repository

class BookingViewModel(val repository: Repository) : ViewModel() {
    private val _vehicle = MutableLiveData<Vehicle>()
    val vehicle: LiveData<Vehicle> get() = _vehicle

    private val _seats = MutableLiveData<List<Seat>>()
    val seats: LiveData<List<Seat>> get() = _seats

    fun selectVehicle(type: String) {
        _vehicle.value = when (type.toLowerCase()) {
            "innova" -> Innova()
            "bus" -> Bus()
            "car" -> Car()
            else -> throw IllegalArgumentException("Unknown vehicle type")
        }
        _seats.value = _vehicle.value?.getSeatLayout()
    }
}
