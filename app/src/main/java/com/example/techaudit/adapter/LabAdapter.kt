package com.example.techaudit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit.databinding.ItemLabBinding
import com.example.techaudit.model.Laboratorio

class LabAdapter(
    private var list: List<Laboratorio>,
    private val onClick: (Laboratorio) -> Unit
) : RecyclerView.Adapter<LabAdapter.LabViewHolder>() {

    inner class LabViewHolder(val binding: ItemLabBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabViewHolder {
        val binding = ItemLabBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LabViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: LabViewHolder, position: Int) {
        val lab = list[position]
        holder.binding.tvLabNombre.text = lab.nombre
        holder.binding.tvLabEdificio.text = "Edificio: ${lab.edificio}"
        holder.binding.root.setOnClickListener { onClick(lab) }
    }

    fun update(newList: List<Laboratorio>) {
        list = newList
        notifyDataSetChanged()
    }
}
