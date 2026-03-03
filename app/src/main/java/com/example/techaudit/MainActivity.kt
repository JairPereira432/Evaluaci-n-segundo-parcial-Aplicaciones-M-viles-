package com.example.techaudit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.techaudit.adapter.LabAdapter
import com.example.techaudit.databinding.ActivityMainBinding
import com.example.techaudit.databinding.DialogAddLabBinding
import com.example.techaudit.model.Laboratorio
import com.example.techaudit.viewmodel.LabViewModel
import com.example.techaudit.viewmodel.LabViewModelFactory
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: LabViewModel by viewModels {
        LabViewModelFactory((application as TechAuditApp).repository)
    }
    private lateinit var adapter: LabAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()

        binding.fabAddLab.setOnClickListener { showAddLabDialog() }
        binding.btnSincronizar.setOnClickListener { viewModel.syncData() }
    }

    private fun setupRecyclerView() {
        adapter = LabAdapter(emptyList()) { lab ->
            val intent = Intent(this, EquipoActivity::class.java)
            intent.putExtra("LAB_ID", lab.id)
            intent.putExtra("LAB_NOMBRE", lab.nombre)
            startActivity(intent)
        }
        binding.rvLaboratorios.layoutManager = LinearLayoutManager(this)
        binding.rvLaboratorios.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.allLaboratorios.observe(this) { labs ->
            adapter.update(labs)
        }

        viewModel.syncing.observe(this) { isSyncing ->
            binding.progressBar.visibility = if (isSyncing) View.VISIBLE else View.GONE
            binding.btnSincronizar.isEnabled = !isSyncing
        }

        viewModel.syncResult.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAddLabDialog() {
        val dialogBinding = DialogAddLabBinding.inflate(LayoutInflater.from(this))
        AlertDialog.Builder(this)
            .setTitle("Nuevo Laboratorio")
            .setView(dialogBinding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = dialogBinding.etNombreLab.text.toString()
                val edificio = dialogBinding.etEdificioLab.text.toString()
                if (nombre.isNotBlank() && edificio.isNotBlank()) {
                    viewModel.insert(Laboratorio(UUID.randomUUID().toString(), nombre, edificio))
                } else {
                    Toast.makeText(this, "Campos obligatorios", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
