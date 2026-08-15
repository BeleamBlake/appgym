package com.example.appgym

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Clases : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_clases, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvClases = view.findViewById<RecyclerView>(R.id.rv_clases)

        // TODO: reemplazar por los datos reales que traigas de tu API
        // (GET /api/clases del dia seleccionado, por ejemplo)
        val listaClases = listOf(
            ClaseGrupal("Yoga", "Ana Lopez", "08:00 - 09:00", "Sala A", 12, 20, "yoga"),
            ClaseGrupal("Spinning", "Carlos Ruiz", "09:30 - 10:30", "Sala B", 15, 18, "spinning"),
            ClaseGrupal("Funcional", "Maria Torres", "11:00 - 12:00", "Sala C", 8, 15, "funcional"),
            ClaseGrupal("Pilates", "Ana Lopez", "17:00 - 18:00", "Sala A", 10, 20, "pilates")
        )

        val adapter = ClasesAdapter(listaClases) { claseSeleccionada ->
            // Aca abrimos el detalle de la clase tocada
            // (descripcion, entrenador, duracion, capacidad, boton reservar/cancelar)
        }

        rvClases.layoutManager = LinearLayoutManager(requireContext())
        rvClases.adapter = adapter

        // Espacio entre tarjetas, sin tener que tocar el XML de cada item
        rvClases.addItemDecoration(EspaciadoVertical(12))
    }
}