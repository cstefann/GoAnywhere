package com.example.goanywhere.network

import com.example.goanywhere.db.TripDAO
import com.example.goanywhere.models.Coordinates
import com.example.goanywhere.models.RouteModel
import com.example.goanywhere.models.Trip
import com.example.goanywhere.shareable.coordinatesData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RetroRepository @Inject constructor(private val dataAPI: DataAPI,
                                          private val tripDao: TripDAO) {

    fun getTripFromDB(): List<Trip> {
        return tripDao.getTrip()
    }

    fun insertTripInDB(trip: Trip) {
        tripDao.insertTrip(trip)
    }

     fun provideCoordsApiCall(q1: String) {
        val call: Call<Coordinates> = dataAPI.provideCoords(q1)
        call.enqueue(object : Callback<Coordinates> {
            override fun onResponse(
                call: Call<Coordinates>,
                response: Response<Coordinates>
            ) {
                if(response.isSuccessful) {
                    coordinatesData.latitude.add(response.body()!!.latitude)
                    coordinatesData.longitude.add(response.body()!!.longitude)
                }
            }

            override fun onFailure(call: Call<Coordinates>, t: Throwable) {
                return
            }
        })
    }

    suspend fun provideLastStatusApiCall(q1: String): Coordinates {
        val response: Response<Coordinates> = dataAPI.provideLastStatus(q1)
        return response.body()!!
    }

    suspend fun getRoutesApiCall(q1: String, q2: String, q3: String, q4: String): List<RouteModel> {
        val response: Response<List<RouteModel>> = dataAPI.getRoutes(q1, q2, q3, q4)
        return response.body()!!
    }

    fun routeInfoApiCall(q1: String, q2: String, q3: String, q4: String, q5: String, q6: String) {
        val call: Call<Trip> = dataAPI.infoRoute(q1, q2, q3, q4, q5, q6)
        call.enqueue(object : Callback<Trip> {
            override fun onResponse(
                call: Call<Trip>,
                response: Response<Trip>
            ) {
                if(response.isSuccessful) {
                    tripDao.deleteAllTrips()
                    insertTripInDB(response.body()!!)
                    return
                }
            }

            override fun onFailure(call: Call<Trip>, t: Throwable) {
                return
            }
        })
    }
}
