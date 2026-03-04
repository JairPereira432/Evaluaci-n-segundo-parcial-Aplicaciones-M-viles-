package com.example.techaudit.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit.databinding.ItemAuditBinding
import com.example.techaudit.model.Equipo
import com.example.techaudit.model.EstadoEquipo

class EquipoAdapter(
    private var list: List<Equipo>,
    private val onClick: (Equipo) -> Unit
) : RecyclerView.Adapter<EquipoAdapter.EquipoViewHolder>() {

    inner class EquipoViewHolder(val binding: ItemAuditBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoViewHolder {
        val binding = ItemAuditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EquipoViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: EquipoViewHolder, position: Int) {
        val equipo = list[position]
        holder.binding.tvNombreEquipo.text = equipo.nombre
        holder.binding.tvEstadoLabel.text = equipo.estado.name
        
        val color = when(equipo.estado) {
            EstadoEquipo.OPERATIVO -> Color.parseColor("#4CAF50")
            EstadoEquipo.DANIADO -> Color.parseColor("#F44336")
            EstadoEquipo.PENDIENTE -> Color.parseColor("#9E9E9E")
        }
        holder.binding.viewStatusColor.setBackgroundColor(color)
        holder.binding.tvEstadoLabel.setTextColor(color)
        
        holder.binding.root.setOnClickListener { onClick(equipo) }
    }

    fun update(newList: List<Equipo>) {
        list = newList
        notifyDataSetChanged()
    }

    fun getEquipoAt(position: Int): Equipo = list[position]
}
