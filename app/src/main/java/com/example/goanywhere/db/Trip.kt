package com.example.goanywhere.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip_table")
data class Trip(
    var arrival_time_current_stop: Long = 0L,
    var arrival_time_dest: Long = 0L,
    var vehicle_id: Int = 0,
    var number_of_stops_to_destination: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}