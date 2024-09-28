package com.varcassoftware.ridercabapp.customers.data

import com.varcassoftware.ridercabapp.customers.models.Seat

class Bus : Vehicle() {
    override fun getSeatLayout(): List<Seat> {
        // Example seating layout for a bus
        return listOf(
            Seat(seatNumber = 1),
            Seat(seatNumber = 2),
            Seat(seatNumber = 3),
            Seat(seatNumber = 4),
            Seat(seatNumber = 5),
            Seat(seatNumber = 6),
            Seat(seatNumber = 7),
            Seat(seatNumber = 8),
            Seat(seatNumber = 9),
            Seat(seatNumber = 10)
            // Add more seats as needed
        )
    }
}
