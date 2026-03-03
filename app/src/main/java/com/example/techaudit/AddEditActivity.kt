package com.example.techaudit

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.techaudit.databinding.ActivityAddEditBinding
import com.example.techaudit.model.Equipo
import com.example.techaudit.model.EstadoEquipo
import kotlinx.coroutines.launch
import java.util.UUID

class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinner()

        binding.btnGuardar.setOnClickListener {
            guardarRegistro()
        }
    }

    private fun setupSpinner() {
        val estados = EstadoEquipo.values()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spEstado.adapter = adapter
    }

    private fun guardarRegistro() {
        val nombre = binding.etNombre.text.toString() // Corregido ID
        val labId = intent.getStringExtra("LAB_ID") ?: ""

        if (nombre.isBlank() || labId.isBlank()) {
            Toast.makeText(this, "Nombre es obligatorio", Toast.LENGTH_SHORT).show()
            return
        }

        val estadoSeleccionado = binding.spEstado.selectedItem as EstadoEquipo
        val nuevoEquipo = Equipo(
            id = UUID.randomUUID().toString(),
            nombre = nombre,
            estado = estadoSeleccionado,
            laboratorioId = labId
        )

        val repository = (application as TechAuditApp).repository

        lifecycleScope.launch {
            repository.insertEquipo(nuevoEquipo)
            Toast.makeText(this@AddEditActivity, "Guardado!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
