package com.example.goanywhere.di

import android.app.Application
import com.example.goanywhere.db.TripDAO
import com.example.goanywhere.db.TripDb
import com.example.goanywhere.network.DataAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTripsDatabase(context: Application): TripDb {
        return TripDb.getAppDBInstance(context)
    }

    @Provides
    @Singleton
    fun provideTripDAO(appDatabase: TripDb): TripDAO {
        return appDatabase.getTripDAO()
    }

    @Provides
    @Singleton
    fun getRetroServiceInstance(retrofit: Retrofit): DataAPI {
        return retrofit.create(DataAPI::class.java)
    }

    @Provides
    @Singleton
    fun getRetroInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}