package com.example.techaudit.data

import com.example.techaudit.model.Equipo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("equipos") // Nombre del endpoint en MockAPI
    suspend fun syncEquipos(@Body equipos: List<Equipo>): Response<Void>
}
