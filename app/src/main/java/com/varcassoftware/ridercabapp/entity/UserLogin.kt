package com.varcassoftware.ridercabapp.entity

data class UserLogin(
    var userName: String,
    var password: String,
    var deviceId: String="-1",
    var token: String="",
)

