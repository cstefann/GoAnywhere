package com.example.goanywhere.di

import android.content.Context
import androidx.room.Room
import com.example.goanywhere.DataAPI
import com.example.goanywhere.db.TripDb
import com.example.goanywhere.others.Strings.TRIPS_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideTripsDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        TripDb::class.java,
        TRIPS_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideTripDAO(db: TripDb) = db.getTripDAO()
}