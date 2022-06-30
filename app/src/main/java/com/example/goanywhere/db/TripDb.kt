package com.example.goanywhere.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.goanywhere.models.Trip
import com.example.goanywhere.others.Strings

@Database(
    entities = [Trip::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TripDb: RoomDatabase(){
    abstract fun getTripDAO() : TripDAO

    companion object {
        private var DB_INSTANCE: TripDb? = null

        fun getAppDBInstance(context: Context): TripDb {
            if(DB_INSTANCE == null) {
                DB_INSTANCE =  Room.databaseBuilder(context.applicationContext, TripDb::class.java,
                    Strings.TRIPS_DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return DB_INSTANCE!!
        }
    }
}