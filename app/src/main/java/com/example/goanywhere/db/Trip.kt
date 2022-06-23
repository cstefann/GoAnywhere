package com.example.goanywhere.db

import androidx.room.Entity
import androidx.room.PrimaryKey

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
//    val ruta: List<Ruta>,
    val vehicul: String
)