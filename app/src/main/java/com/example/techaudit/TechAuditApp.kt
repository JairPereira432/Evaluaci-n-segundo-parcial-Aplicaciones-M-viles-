package com.example.techaudit

import android.app.Application
import com.example.techaudit.data.AuditDatabase
import com.example.techaudit.data.AuditRepository
import com.example.techaudit.data.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TechAuditApp : Application() {

    // Base de datos de Room
    private val database: AuditDatabase by lazy {
        AuditDatabase.getDatabase(this)
    }

    // Cliente de Retrofit con tu URL real de MockAPI
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://69a742882cd1d05526904d47.mockapi.io/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    // Repositorio central
    val repository: AuditRepository by lazy {
        AuditRepository(database.auditDao(), apiService)
    }
}
