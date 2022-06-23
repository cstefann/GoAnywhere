package com.example.goanywhere

import com.example.goanywhere.db.RouteModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface  DataAPI {

    @GET("/getroutes")
    suspend fun getRoutes(@Query("currentlat") q1: String,
                  @Query("currentlpn") q2: String,
                  @Query("destlat") q3: String,
                  @Query("destlon") q4: String): Response<List<RouteModel>>
}