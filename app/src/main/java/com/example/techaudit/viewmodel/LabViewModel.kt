package com.example.techaudit.viewmodel

import androidx.lifecycle.*
import com.example.techaudit.data.AuditRepository
import com.example.techaudit.model.Laboratorio
import kotlinx.coroutines.launch

class LabViewModel(private val repository: AuditRepository) : ViewModel() {

    val allLaboratorios: LiveData<List<Laboratorio>> = repository.allLaboratorios.asLiveData()

    // Indicador de carga para sincronización
    private val _syncing = MutableLiveData<Boolean>(false)
    val syncing: LiveData<Boolean> get() = _syncing

    private val _syncResult = MutableLiveData<String>()
    val syncResult: LiveData<String> get() = _syncResult

    fun insert(lab: Laboratorio) = viewModelScope.launch {
        repository.insertLaboratorio(lab)
    }

    fun syncData() = viewModelScope.launch {
        _syncing.value = true
        try {
            val response = repository.syncWithCloud()
            if (response.isSuccessful) {
                _syncResult.value = "Sincronización Exitosa"
            } else {
                _syncResult.value = "Error: ${response.code()}"
            }
        } catch (e: Exception) {
            _syncResult.value = "Error: ${e.message}"
        } finally {
            _syncing.value = false
        }
    }
}

class LabViewModelFactory(private val repository: AuditRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LabViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LabViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
