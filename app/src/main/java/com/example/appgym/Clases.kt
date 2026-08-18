package com.example.appgym

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appgym.R.id.lista_clases

class ClasesFragment : Fragment() {

    private lateinit var rvClases: RecyclerView
    private lateinit var adapter: ClasesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el layout de tu fragmento
        val view = inflater.inflate(R.layout.fragment_clases, container, false)

        //rvClases.layoutManager = LinearLayoutManager(requireContext())
        // 1. Inicializamos el RecyclerView
        rvClases = view.findViewById(lista_clases)
        rvClases.layoutManager = LinearLayoutManager(requireContext())

        // 2. Creamos datos de prueba (Mock Data) de tus entrenadores
        val misClases = listOf(
            ClasesGym("Zumba", "Entrenador: María", "5/20"),
            ClasesGym("Spinning", "Entrenador: Carlos", "12/15"),
            ClasesGym("Crossfit", "Entrenador: Roberto", "10/10")
        )

        // 3. Conectamos el adaptador al RecyclerView
        adapter = ClasesAdapter(misClases)
        rvClases.adapter = adapter

        return view
    }
}