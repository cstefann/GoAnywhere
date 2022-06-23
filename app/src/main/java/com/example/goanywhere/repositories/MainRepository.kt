package com.example.goanywhere.repositories

import com.example.goanywhere.db.Trip
import com.example.goanywhere.db.TripDAO
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val tripDAO: TripDAO
){
    suspend fun insertTrip(trip: Trip) = tripDAO.insertTrip(trip)
    suspend fun deleteTrip(trip: Trip) = tripDAO.deleteTrip(trip)
    fun getAllTripsSortedByDistanceToDestination() = tripDAO.getAllTripsSortedByDistanceToDestination()
    fun getAllTripsSortedBNumberOfStopsTillDestination() = tripDAO.getAllTripsSortedBNumberOfStopsTillDestination()
    fun getAllTripsSortedByArrivalTimeToDest() = tripDAO.getAllTripsSortedByArrivalTimeToDest()

}