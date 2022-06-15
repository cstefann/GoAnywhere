package com.example.goanywhere.repositories

import com.example.goanywhere.db.Trip
import com.example.goanywhere.db.TripDAO
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val tripDAO: TripDAO
){
    suspend fun insertTrip(trip: Trip) = tripDAO.insertTrip(trip)
    suspend fun deleteTrip(trip: Trip) = tripDAO.deleteTrip(trip)
    fun getAllTripsSortedByTimeCurrentLocation() = tripDAO.getAllTripsSortedByTimeCurrentLocation()
    fun getAllTripsSortedByTimeDest() = tripDAO.getAllTripsSortedByTimeDest()
    fun getAllTripsSortedByVehicleID() = tripDAO.getAllTripsSortedByVehicleID()
    fun getAllTripsSortedByStopsToDest() = tripDAO.getAllTripsSortedByStopsToDest()

}