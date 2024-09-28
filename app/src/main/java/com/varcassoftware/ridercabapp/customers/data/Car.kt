package com.varcassoftware.ridercabapp.customers.data

import com.varcassoftware.ridercabapp.customers.models.Seat

class Car : Vehicle() {
    override fun getSeatLayout(): List<Seat> {
        // Example seating layout for a car
        return listOf(
            Seat(seatNumber = 1),
            Seat(seatNumber = 2),
            Seat(seatNumber = 3),
            Seat(seatNumber = 4)
            // Add more seats as needed
        )
    }
}
