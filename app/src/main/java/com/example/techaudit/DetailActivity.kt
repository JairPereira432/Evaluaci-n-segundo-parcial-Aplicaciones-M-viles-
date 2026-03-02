package com.example.techaudit

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.techaudit.databinding.ActivityDetailBinding
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditStatus

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXTRA_ITEM", AuditItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXTRA_ITEM")
        }

        item?.let {
            mostrarDetalles(it)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun mostrarDetalles(item: AuditItem) {
        binding.tvDetalleNombre.text = item.nombre
        binding.tvDetalleId.text = "ID: ${item.id.substring(0, 8)}"
        binding.tvDetalleUbicacion.text = item.ubicación
        binding.tvDetalleFecha.text = item.fechaRegistro
        binding.tvDetalleNotas.text = item.notas.ifEmpty { "Sin observaciones." }

        val color = when (item.estado) {
            AuditStatus.OPERATIVO -> Color.parseColor("#4CAF50")
            AuditStatus.PENDIENTE -> Color.parseColor("#9E9E9E")
            AuditStatus.DANIADO -> Color.parseColor("#F44336")
            AuditStatus.NO_ENCONTRADO -> Color.BLACK
        }
        binding.viewHeaderStatus.setBackgroundColor(color)

        title = "Detalle: ${item.estado.name}"
    }
}
