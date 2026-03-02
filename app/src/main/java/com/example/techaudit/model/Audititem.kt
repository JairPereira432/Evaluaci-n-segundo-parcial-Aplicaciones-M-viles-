package com.example.techaudit.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

enum class AuditStatus {
    PENDIENTE,
    OPERATIVO,
    DANIADO,
    NO_ENCONTRADO
}

@Entity(tableName = "equipos")
@Parcelize
data class AuditItem(
    @PrimaryKey
    val id: String, // UUID o Codigo de barras
    val nombre: String,
    val ubicación: String,
    val fechaRegistro: String,
    var estado: AuditStatus = AuditStatus.PENDIENTE,
    var notas: String = "",
    var fotoUri: String? = null
) : Parcelable
