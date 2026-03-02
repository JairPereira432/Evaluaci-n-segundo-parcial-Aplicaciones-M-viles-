package com.example.techaudit

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.techaudit.adapter.AuditAdapter
import com.example.techaudit.data.AuditDatabase
import com.example.techaudit.databinding.ActivityMainBinding
import com.example.techaudit.model.AuditItem
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: AuditDatabase
    private lateinit var adapter: AuditAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar base de datos
        database = (application as TechAuditApp).database

        setupRecyclerView()

        // Configurar botones
        binding.fabAgregar.setOnClickListener {
            val intent = Intent(this, AddEditActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        cargaDatosDeBaseDeDatos()
    }

    private fun setupRecyclerView() {
        adapter = AuditAdapter(mutableListOf()) { itemSeleccionado ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("EXTRA_ITEM", itemSeleccionado)
            startActivity(intent)
        }

        binding.rvAuditoria.apply {
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun cargaDatosDeBaseDeDatos() {
        lifecycleScope.launch {
            val datos = database.auditDao().getAllItems()
            if (datos.isEmpty()) {
                Toast.makeText(this@MainActivity, "No hay registros aún", Toast.LENGTH_SHORT).show()
                adapter.actualizarLista(emptyList())
            } else {
                adapter.actualizarLista(datos)
            }
        }
    }
}
