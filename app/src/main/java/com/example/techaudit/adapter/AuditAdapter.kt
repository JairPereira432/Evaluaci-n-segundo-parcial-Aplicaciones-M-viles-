package com.example.techaudit.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit.databinding.ItemAuditBinding
import com.example.techaudit.model.AuditStatus
import com.example.techaudit.model.AuditItem

class AuditAdapter(
    val listaAuditoria: MutableList<AuditItem>,
    private val onItemSelected: (AuditItem) -> Unit
) : RecyclerView.Adapter<AuditAdapter.AuditViewHolder>() {

    inner class AuditViewHolder(val binding: ItemAuditBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuditViewHolder {
        val binding = ItemAuditBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AuditViewHolder(binding)
    }

    override fun getItemCount(): Int = listaAuditoria.size

    fun actualizarLista(nuevaLista: List<AuditItem>) {
        listaAuditoria.clear()
        listaAuditoria.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: AuditViewHolder, position: Int) {
        val item = listaAuditoria[position]

        holder.binding.tvNombreEquipo.text = item.nombre
        holder.binding.tvUbicacion.text = item.ubicación
        holder.binding.tvEstadoLabel.text = item.estado.name

        val colorEstado = when (item.estado) {
            AuditStatus.OPERATIVO -> Color.parseColor("#4CAF50")
            AuditStatus.PENDIENTE -> Color.parseColor("#9E9E9E")
            AuditStatus.DANIADO -> Color.parseColor("#F44336")
            AuditStatus.NO_ENCONTRADO -> Color.BLACK
        }

        holder.binding.viewStatusColor.setBackgroundColor(colorEstado)
        holder.binding.tvEstadoLabel.setTextColor(colorEstado)

        holder.itemView.setOnClickListener {
            onItemSelected(item)
        }
    }
}
