package com.varcassoftware.ridercabapp.customers.data

import com.varcassoftware.ridercabapp.customers.models.Seat

abstract class Vehicle {
    abstract fun getSeatLayout(): List<Seat>
}