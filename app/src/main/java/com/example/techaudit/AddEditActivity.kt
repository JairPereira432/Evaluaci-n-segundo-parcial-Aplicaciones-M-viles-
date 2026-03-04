package com.example.techaudit

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.techaudit.databinding.ActivityAddEditBinding
import com.example.techaudit.model.Equipo
import com.example.techaudit.model.EstadoEquipo
import com.example.techaudit.model.Laboratorio
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding
    private var modo: String? = null
    private var labId: String? = null
    private var equipoParaEditar: Equipo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        modo = intent.getStringExtra("TIPO_MODO")
        labId = intent.getStringExtra("LAB_ID")
        equipoParaEditar = intent.getParcelableExtra("EQUIPO_EDITAR")

        setupUI()

        binding.btnGuardar.setOnClickListener {
            if (modo == "LABORATORIO") {
                guardarLaboratorio()
            } else {
                guardarEquipo()
            }
        }
    }

    private fun setupUI() {
        if (modo == "LABORATORIO") {
            title = "Nuevo Laboratorio"
            binding.etUbicacion.hint = "Edificio (Ej: Bloque B)"
            binding.spEstado.visibility = View.GONE
            binding.etNotas.visibility = View.GONE
        } else {
            title = if (equipoParaEditar != null) "Editar Equipo" else "Nuevo Equipo"
            binding.etUbicacion.hint = "Ubicación exacta dentro del Lab"
            setupSpinner()
            
            equipoParaEditar?.let {
                binding.etNombre.setText(it.nombre)
                binding.etUbicacion.setText(it.ubicacion)
                binding.etNotas.setText(it.notas)
                binding.spEstado.setSelection(EstadoEquipo.values().indexOf(it.estado))
            }
        }
    }

    private fun setupSpinner() {
        val estados = EstadoEquipo.values()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spEstado.adapter = adapter
    }

    private fun guardarLaboratorio() {
        val nombre = binding.etNombre.text.toString()
        val edificio = binding.etUbicacion.text.toString()

        if (nombre.isBlank() || edificio.isBlank()) {
            Toast.makeText(this, "Nombre y Edificio son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevoLab = Laboratorio(UUID.randomUUID().toString(), nombre, edificio)
        val repository = (application as TechAuditApp).repository

        lifecycleScope.launch {
            repository.insertLaboratorio(nuevoLab)
            finish()
        }
    }

    private fun guardarEquipo() {
        val nombre = binding.etNombre.text.toString()
        val ubicacion = binding.etUbicacion.text.toString()
        val notas = binding.etNotas.text.toString()
        val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

        if (nombre.isBlank() || ubicacion.isBlank()) {
            Toast.makeText(this, "Nombre y Ubicación son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val estado = binding.spEstado.selectedItem as EstadoEquipo
        val repository = (application as TechAuditApp).repository

        lifecycleScope.launch {
            if (equipoParaEditar != null) {
                repository.updateEquipo(equipoParaEditar!!.copy(
                    nombre = nombre, 
                    ubicacion = ubicacion, 
                    notas = notas, 
                    estado = estado
                ))
            } else {
                val nuevoEquipo = Equipo(
                    id = UUID.randomUUID().toString(),
                    nombre = nombre,
                    ubicacion = ubicacion,
                    fechaRegistro = fechaActual,
                    notas = notas,
                    estado = estado,
                    laboratorioId = labId!!
                )
                repository.insertEquipo(nuevoEquipo)
            }
            finish()
        }
    }
}
