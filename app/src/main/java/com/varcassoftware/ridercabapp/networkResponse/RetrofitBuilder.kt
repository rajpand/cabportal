package com.varcassoftware.ridercabapp.networkResponse

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private const val BASE_URL = "https://api.example.com/"

    private val client: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY  // Set desired log level

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)  // Add logging interceptor
            // Add other interceptors if needed (e.g., header interceptor)
            .build()
    }

    // Create Retrofit instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: RetrofitService by lazy {
        retrofit.create(RetrofitService::class.java)
    }

}
