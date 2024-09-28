package com.varcassoftware.ridercabapp.customers.models

data class Seat(
    val seatNumber: Int,       // Unique number or identifier for the seat
    var isBooked: Boolean = false,  // Indicates if the seat is booked
    val seatType: SeatType = SeatType.REGULAR // Optional: Type of seat (e.g., Regular, VIP)
)