package com.example.goanywhere.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Trip::class],
    version = 1,
    exportSchema = false
)
abstract class TripDb: RoomDatabase(){
    abstract fun getTripDAO() : TripDAO
}