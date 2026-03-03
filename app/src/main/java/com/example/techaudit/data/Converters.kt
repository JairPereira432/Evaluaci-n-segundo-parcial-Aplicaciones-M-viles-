package com.example.techaudit.data

import androidx.room.TypeConverter
import com.example.techaudit.model.EstadoEquipo

class Converters {
    @TypeConverter
    fun fromEstado(estado: EstadoEquipo): String {
        return estado.name
    }

    @TypeConverter
    fun toEstado(value: String): EstadoEquipo {
        return try {
            EstadoEquipo.valueOf(value)
        } catch (e: Exception) {
            EstadoEquipo.PENDIENTE
        }
    }
}
