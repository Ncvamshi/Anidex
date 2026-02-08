package com.example.anidex.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Use your WLAN IP here
    private const val BASE_URL = "http://10.0.2.2:8000/"// 10.0.2.2 is 'localhost' for Android Emulators

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}