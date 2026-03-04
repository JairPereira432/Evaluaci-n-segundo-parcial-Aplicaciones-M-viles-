package com.example.techaudit.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

enum class EstadoEquipo {
    OPERATIVO,
    DANIADO,
    PENDIENTE
}

@Entity(
    tableName = "equipos",
    foreignKeys = [
        ForeignKey(
            entity = Laboratorio::class,
            parentColumns = ["id"],
            childColumns = ["laboratorioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("laboratorioId")]
)
@Parcelize
data class Equipo(
    @PrimaryKey
    val id: String,
    val nombre: String,
    val ubicacion: String,      // Nuevo campo
    val fechaRegistro: String,  // Nuevo campo
    val notas: String,          // Nuevo campo
    val estado: EstadoEquipo = EstadoEquipo.PENDIENTE,
    val laboratorioId: String
) : Parcelable
