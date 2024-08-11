package com.example.app.model

import com.varcassoftware.ridercabapp.utility.GlobalClasses
import java.util.Date

data class CustomerRegistration(
    val firstName: String="",
    val email: String="",
    val mobile: String="",
    val profilePic: String="string",
    val aadhaar: String="",
    val password: String="",
)
