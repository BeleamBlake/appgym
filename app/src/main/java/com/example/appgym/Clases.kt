package com.example.appgym

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ClasesFragment : Fragment() {

    private lateinit var rvClases: RecyclerView
    private lateinit var adapter: ClasesAdapter
    private val lista_clases = mutableListOf<ClasesGym>()
    private val db = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el layout de tu fragmento
        val view = inflater.inflate(R.layout.fragment_clases, container, false)
        rvClases = view.findViewById(R.id.lista_clases)

        // 1. Inicializamos el RecyclerView
        rvClases.layoutManager = LinearLayoutManager(requireContext())
        // 3. Conectamos el adaptador al RecyclerView
        adapter = ClasesAdapter(lista_clases)
        rvClases.adapter = adapter

        cargarClasesDesdeFirestore()
        return view
    }

    private fun cargarClasesDesdeFirestore() {
        // Consultamos la colección "clases" que se ve en tu captura de Firebase
        db.collection("clases")
            .get()
            .addOnSuccessListener { result ->
                lista_clases.clear() // Limpiamos por si acaso
                for (document in result) {
                    // Extraemos los campos tal cual los guardaste en Firestore
                    val nombre = document.getString("nombre") ?: "Clase sin nombre"
                    val entrenadorId = document.getString("entrenador") ?: "Sin entrenador"
                    val hora = document.getString("hora") ?: "00:00"
                    val capacidad = document.getLong("capacidad")?.toInt() ?: 20

                    // Como tu modelo actual usa "lugares" (ej. "5/20"), puedes armarlo dinámicamente o adaptarlo
                    val lugaresDisponibles = "0/$capacidad" // O el valor que lleves en reservas
                    // Creamos el objeto con los datos de Firebase
                    val claseGym = ClasesGym(
                        nombreClase = nombre,
                        nombreEntrenador = "ID: $entrenadorId", // O puedes mapearlo al nombre real del entrenador si lo prefieres
                        horaClase = hora,
                        numeroLugares = lugaresDisponibles
                    )

                    lista_clases.add(claseGym)
                }

                // Notificamos al adaptador que los datos llegaron para que actualice la pantalla
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("ClasesFragment", "Error al cargar clases", e)
            }
    }
}