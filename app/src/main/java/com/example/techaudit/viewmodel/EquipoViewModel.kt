package com.example.techaudit.viewmodel

import androidx.lifecycle.*
import com.example.techaudit.data.AuditRepository
import com.example.techaudit.model.Equipo
import kotlinx.coroutines.launch

class EquipoViewModel(private val repository: AuditRepository) : ViewModel() {

    private val _labId = MutableLiveData<String>()

    val equipos: LiveData<List<Equipo>> = _labId.switchMap { id ->
        repository.getEquipos(id).asLiveData()
    }

    fun setLabId(id: String) {
        _labId.value = id
    }

    fun insert(equipo: Equipo) = viewModelScope.launch {
        repository.insertEquipo(equipo)
    }

    fun update(equipo: Equipo) = viewModelScope.launch {
        repository.updateEquipo(equipo)
    }

    fun delete(equipo: Equipo) = viewModelScope.launch {
        repository.deleteEquipo(equipo)
    }
}

class EquipoViewModelFactory(private val repository: AuditRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EquipoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EquipoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
