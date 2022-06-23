package com.example.goanywhere.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TripDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrip(trip: Trip)

    @Delete
    suspend fun deleteTrip(trip: Trip)

    @Query("SELECT * FROM trip_table ORDER BY distanta ASC")
    fun getAllTripsSortedByDistanceToDestination(): LiveData<List<Trip>>

    @Query("SELECT * FROM trip_table ORDER BY nrStatii ASC")
    fun getAllTripsSortedBNumberOfStopsTillDestination(): LiveData<List<Trip>>

    @Query("SELECT * FROM trip_table ORDER BY arrivalTime ASC")
    fun getAllTripsSortedByArrivalTimeToDest(): LiveData<List<Trip>>
}