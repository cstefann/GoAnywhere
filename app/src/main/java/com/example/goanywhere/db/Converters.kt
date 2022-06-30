package com.example.goanywhere.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.goanywhere.models.Ruta
import timber.log.Timber

class Converters {
    @TypeConverter
    fun fromListRuta(ruta : List<Ruta>): String {
        val list: MutableList<String> = mutableListOf()
        for (i in  ruta){
            list.add(i.statie)
        }
        return list.toString()
    }

    @TypeConverter
    fun toRuta(statie: String): List<Ruta> {
        val ruta = Ruta(statie)
        return listOf(ruta)
    }
}