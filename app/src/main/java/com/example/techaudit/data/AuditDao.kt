package com.example.techaudit.data

import androidx.room.*
import com.example.techaudit.model.Laboratorio
import com.example.techaudit.model.Equipo
import kotlinx.coroutines.flow.Flow

@Dao
interface AuditDao {
    // Laboratorios
    @Query("SELECT * FROM laboratorios")
    fun getAllLaboratorios(): Flow<List<Laboratorio>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaboratorio(lab: Laboratorio)

    // Equipos filtrados por Laboratorio
    @Query("SELECT * FROM equipos WHERE laboratorioId = :labId")
    fun getEquiposByLab(labId: String): Flow<List<Equipo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquipo(equipo: Equipo)

    @Update
    suspend fun updateEquipo(equipo: Equipo)

    @Delete
    suspend fun deleteEquipo(equipo: Equipo)

    // Para sincronización Cloud
    @Query("SELECT * FROM equipos")
    suspend fun getAllEquiposSync(): List<Equipo>
}
