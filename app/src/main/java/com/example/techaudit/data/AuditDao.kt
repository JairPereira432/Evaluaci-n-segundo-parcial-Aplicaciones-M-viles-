package com.example.techaudit.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.techaudit.model.AuditItem

@Dao
interface AuditDao {

    @Query("SELECT * FROM equipos ORDER BY fechaRegistro DESC")
    suspend fun getAllItems(): List<AuditItem>

    @Query("SELECT * FROM equipos WHERE id = :id")
    suspend fun getById(id: String): AuditItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AuditItem)

    @Update
    suspend fun update(item: AuditItem)

    @Delete
    suspend fun deleteAll(item: AuditItem)
}
