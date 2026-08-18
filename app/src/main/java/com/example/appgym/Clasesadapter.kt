package com.example.appgym

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClasesAdapter(
    private val lista: List<ClaseGrupal>,
    private val onClickClase: (ClaseGrupal) -> Unit
) : RecyclerView.Adapter<ClasesAdapter.ClaseViewHolder>() {

    inner class ClaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fondoIcono: FrameLayout = view.findViewById(R.id.fondo_icono_clase)
        val icono: ImageView = view.findViewById(R.id.img_icono_clase)
        val nombre: TextView = view.findViewById(R.id.txt_nombre_clase)
        val instructor: TextView = view.findViewById(R.id.txt_instructor_clase)
        val horario: TextView = view.findViewById(R.id.txt_horario_clase)
        val sala: TextView = view.findViewById(R.id.txt_sala_clase)
        val cupo: TextView = view.findViewById(R.id.txt_cupo_clase)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaseViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_clase, parent, false)
        return ClaseViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ClaseViewHolder, position: Int) {
        val clase = lista[position]

        holder.nombre.text = clase.nombre
        holder.instructor.text = clase.instructor
        holder.horario.text = clase.horario
        holder.sala.text = clase.sala
        holder.cupo.text = "${clase.lugaresOcupados}/${clase.lugaresTotales}"

        // icono y color de fondo segun el tipo
        when (clase.tipo) {
            "yoga" -> {
                holder.fondoIcono.setBackgroundResource(R.drawable.bg_icono_lavanda)
                holder.icono.setImageResource(R.drawable.ic_yoga)
            }
            "spinning" -> {
                holder.fondoIcono.setBackgroundResource(R.drawable.bg_icono_verde_claro)
                holder.icono.setImageResource(R.drawable.ic_spinning)
            }
            "funcional" -> {
                holder.fondoIcono.setBackgroundResource(R.drawable.bg_icono_naranja_claro)
                holder.icono.setImageResource(R.drawable.ic_funcional)
            }
            "pilates" -> {
                holder.fondoIcono.setBackgroundResource(R.drawable.bg_icono_azul_claro)
                holder.icono.setImageResource(R.drawable.ic_pilates)
            }
        }

        // si la clase esta llena, el cupo se resalta distinto (capacidad maxima alcanzada)
        if (clase.lugaresOcupados >= clase.lugaresTotales) {
            holder.cupo.setBackgroundResource(R.drawable.bg_badge_verde)
            holder.cupo.text = "Completo"
        }

        holder.itemView.setOnClickListener {
            onClickClase(clase)
        }
    }

    override fun getItemCount(): Int = lista.size
}