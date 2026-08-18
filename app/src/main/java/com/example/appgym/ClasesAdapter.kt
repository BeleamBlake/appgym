package com.example.appgym

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


    class ClasesAdapter(private val listaClases: List<ClasesGym>) : RecyclerView.Adapter<ClasesAdapter.ClaseViewHolder>() {

        // Esta clase interna conecta las vistas de tu claseslista.xml
        class ClaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvNombreClase: TextView = itemView.findViewById(R.id.nombre_clase)

            // NOTA: Asegúrate de que el ID en tu claseslista.xml para el entrenador sea este,
            // o cámbialo por el que le hayas puesto.
            val tvEntrenador: TextView = itemView.findViewById(R.id.nombre_entrenador)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaseViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.claseslista, parent, false)
            return ClaseViewHolder(view)
        }

        override fun onBindViewHolder(holder: ClaseViewHolder, position: Int){
           val clase = listaClases[position]

            holder.tvNombreClase.text = clase.nombreClase
         holder.tvEntrenador.text = clase.nombreEntrenador

        }

        override fun getItemCount(): Int {
            return listaClases.size
        }
    }


