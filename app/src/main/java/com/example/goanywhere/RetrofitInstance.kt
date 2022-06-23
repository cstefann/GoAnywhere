package com.example.goanywhere

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: DataAPI by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DataAPI::class.java)
    }
}