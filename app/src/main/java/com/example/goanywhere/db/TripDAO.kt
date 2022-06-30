package com.example.goanywhere.db

import androidx.room.*
import com.example.goanywhere.models.Trip

@Dao
interface TripDAO {
    @Query("SELECT * FROM trip_table")
    fun getTrip(): List<Trip>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrip(repositoryData: Trip)

    @Query("DELETE FROM trip_table")
    fun deleteAllTrips()
}