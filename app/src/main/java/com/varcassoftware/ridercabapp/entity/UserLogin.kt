package com.varcassoftware.ridercabapp.entity

data class UserLogin(
    var firstName: String,
    var password: String,
    var deviceId: String="-1",
    var token: String="",
)

