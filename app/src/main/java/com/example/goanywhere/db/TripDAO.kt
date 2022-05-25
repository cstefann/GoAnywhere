package com.example.goanywhere.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TripDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: Trip)

    @Delete
    suspend fun deleteTrip(trip: Trip)

    @Query("SELECT * FROM trip_table ORDER BY arrival_time_current_stop ASC")
    fun getAllTripsSortedByTimeCurrentLocation(): LiveData<List<Trip>>

    @Query("SELECT * FROM trip_table ORDER BY arrival_time_dest ASC")
    fun getAllTripsSortedByTimeDest(): LiveData<List<Trip>>

    @Query("SELECT * FROM trip_table ORDER BY vehicle_id ASC")
    fun getAllTripsSortedByVehicleID(): LiveData<List<Trip>>

    @Query("SELECT * FROM trip_table ORDER BY number_of_stops_to_destination ASC")
    fun getAllTripsSortedByStopsToDest(): LiveData<List<Trip>>
} 