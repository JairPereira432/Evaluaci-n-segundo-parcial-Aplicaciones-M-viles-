package com.example.techaudit

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit.adapter.EquipoAdapter
import com.example.techaudit.databinding.ActivityEquipoBinding
import com.example.techaudit.databinding.DialogAddEditEquipoBinding
import com.example.techaudit.model.Equipo
import com.example.techaudit.model.EstadoEquipo
import com.example.techaudit.viewmodel.EquipoViewModel
import com.example.techaudit.viewmodel.EquipoViewModelFactory
import java.util.UUID

class EquipoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEquipoBinding
    private val viewModel: EquipoViewModel by viewModels {
        EquipoViewModelFactory((application as TechAuditApp).repository)
    }
    private lateinit var adapter: EquipoAdapter
    private var currentLabId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEquipoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentLabId = intent.getStringExtra("LAB_ID")
        val labNombre = intent.getStringExtra("LAB_NOMBRE")
        
        supportActionBar?.title = "Equipos: $labNombre"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        currentLabId?.let { viewModel.setLabId(it) }

        setupRecyclerView()
        setupSwipeToDelete()
        observeViewModel()

        binding.fabAddEquipo.setOnClickListener { showAddEditDialog(null) }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupRecyclerView() {
        adapter = EquipoAdapter(emptyList()) { equipo ->
            showAddEditDialog(equipo)
        }
        binding.rvEquipos.layoutManager = LinearLayoutManager(this)
        binding.rvEquipos.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.equipos.observe(this) { equipos ->
            adapter.update(equipos)
        }
    }

    private fun setupSwipeToDelete() {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder): Boolean = false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val equipo = adapter.getEquipoAt(position)
                viewModel.delete(equipo)
                Toast.makeText(this@EquipoActivity, "Equipo eliminado", Toast.LENGTH_SHORT).show()
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.rvEquipos)
    }

    private fun showAddEditDialog(equipo: Equipo?) {
        val dialogBinding = DialogAddEditEquipoBinding.inflate(LayoutInflater.from(this))
        val isEdit = equipo != null

        // Llenar Spinner
        val estados = EstadoEquipo.values()
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estados)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogBinding.spEstado.adapter = spinnerAdapter

        if (isEdit) {
            dialogBinding.etNombreEquipo.setText(equipo?.nombre)
            dialogBinding.spEstado.setSelection(estados.indexOf(equipo?.estado))
        }

        AlertDialog.Builder(this)
            .setTitle(if (isEdit) "Editar Equipo" else "Nuevo Equipo")
            .setView(dialogBinding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = dialogBinding.etNombreEquipo.text.toString()
                val estado = dialogBinding.spEstado.selectedItem as EstadoEquipo
                
                if (nombre.isNotBlank()) {
                    if (isEdit) {
                        viewModel.update(equipo!!.copy(nombre = nombre, estado = estado))
                    } else {
                        viewModel.insert(Equipo(UUID.randomUUID().toString(), nombre, estado, currentLabId!!))
                    }
                } else {
                    Toast.makeText(this, "Nombre obligatorio", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
