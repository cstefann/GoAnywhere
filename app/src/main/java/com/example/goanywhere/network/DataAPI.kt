package com.example.goanywhere.network

import com.example.goanywhere.models.Coordinates
import com.example.goanywhere.models.RouteModel
import com.example.goanywhere.models.Trip
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DataAPI {

    @GET("/providecoords")
    fun provideCoords(@Query("location") q1: String): Call<Coordinates>

    @GET("/status")
    suspend fun provideLastStatus(@Query("vehicle") q1: String): Response<Coordinates>

    @GET("/getroutes")
    suspend fun getRoutes(@Query("currentlat") q1: String,
                          @Query("currentlon") q2: String,
                          @Query("destlat") q3: String,
                          @Query("destlon") q4: String): Response<List<RouteModel>>

    @GET("/inforoute")
    fun infoRoute(@Query("currentlat") q1: String,
                          @Query("currentlon") q2: String,
                          @Query("destlat") q3: String,
                          @Query("destlon") q4: String,
                          @Query("routeName") q5: String,
                          @Query("routeNumber") q6: String): Call<Trip>
}
