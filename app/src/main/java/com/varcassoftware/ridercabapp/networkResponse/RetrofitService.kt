package com.varcassoftware.ridercabapp.networkResponse

import com.example.app.model.CustomerRegistration
import com.varcassoftware.ridercabapp.activity.map.entity.DirectionsResponse
import com.varcassoftware.ridercabapp.entity.UserLogin
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitService {

    @GET("maps/api/directions/json")
    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String
    ): Call<DirectionsResponse>

    @POST("api/Customer/CustomerRegistration")
   suspend fun getCustomerRegistration(@Body customerRegistration: CustomerRegistration): Response<String?>

    @POST("api/CustomerAccount/Login/Login")
     fun saveUserLoginDetails(userLogin: UserLogin): Response<UserLogin?>

}