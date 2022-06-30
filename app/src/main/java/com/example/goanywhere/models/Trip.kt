package com.example.goanywhere.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.goanywhere.db.Converters
import com.google.gson.JsonObject
import retrofit2.Response
import java.util.*

@Entity(tableName = "trip_table")
data class Trip(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val arrivalTime: String,
    val distanta: String,
    val locatie: String,
    val nearestStation: String,
    val nrStatii: String,
    val ora: String,
    val ruta: List<Ruta>,
    val vehicul: String
)