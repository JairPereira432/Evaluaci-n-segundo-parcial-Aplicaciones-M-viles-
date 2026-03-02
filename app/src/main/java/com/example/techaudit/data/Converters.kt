package com.example.techaudit.data

import androidx.room.TypeConverter
import com.example.techaudit.model.AuditStatus

class Converters {

    // De Enum a String (para guardar en la base de datos)
    @TypeConverter
    fun fromStatus(status: AuditStatus): String {
        return status.name
    }

    // De String a Enum (para leer de la base de datos)
    @TypeConverter
    fun toStatus(value: String): AuditStatus {
        return try {
            AuditStatus.valueOf(value)
        } catch (e: IllegalArgumentException) {
            AuditStatus.PENDIENTE // Valor por defecto si hay algún error
        }
    }
}
