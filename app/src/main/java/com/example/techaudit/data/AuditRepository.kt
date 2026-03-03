package com.example.techaudit.data

import com.example.techaudit.model.Laboratorio
import com.example.techaudit.model.Equipo
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class AuditRepository(private val dao: AuditDao, private val api: ApiService) {

    // Laboratorios
    val allLaboratorios: Flow<List<Laboratorio>> = dao.getAllLaboratorios()

    suspend fun insertLaboratorio(lab: Laboratorio) {
        dao.insertLaboratorio(lab)
    }

    // Equipos
    fun getEquipos(labId: String): Flow<List<Equipo>> = dao.getEquiposByLab(labId) // Corregido nombre función

    suspend fun insertEquipo(equipo: Equipo) = dao.insertEquipo(equipo)
    suspend fun updateEquipo(equipo: Equipo) = dao.updateEquipo(equipo)
    suspend fun deleteEquipo(equipo: Equipo) = dao.deleteEquipo(equipo)

    // Sincronización
    suspend fun syncWithCloud(): Response<Void> {
        val allEquipos = dao.getAllEquiposSync()
        return api.syncEquipos(allEquipos)
    }
}
