package com.varcassoftware.ridercabapp.networkResponse

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.security.cert.CertificateException
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

object RetrofitBuilder {

    private const val BASE_URL = "https://ridercab.varcas.org/"
    private const val BASE_URL_GOOGLE = "https://maps.googleapis.com/"

    private val client: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
            }

            override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return arrayOf()
            }
        })

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL").apply {
            init(null, trustAllCerts, java.security.SecureRandom())
        }

        val sslSocketFactory = sslContext.socketFactory

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .callTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()
    }

    // Create a Gson instance with lenient parsing
    private val gson: Gson by lazy {
        GsonBuilder()
            .setLenient()
            .create()
    }

    // Create Retrofit instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // Retrofit instance for Google APIs
    val retrofitForGoogle: RetrofitService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_GOOGLE)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(RetrofitService::class.java)
    }

    val apiService: RetrofitService by lazy {
        retrofit.create(RetrofitService::class.java)
    }
}
