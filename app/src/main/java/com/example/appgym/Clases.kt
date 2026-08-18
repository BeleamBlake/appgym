package com.example.appgym

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        adapter = ClasesAdapter(lista_clases){ claseSeleccionadas->
            realizarReserva(claseSeleccionadas)

        }
        rvClases.adapter = adapter
        cargarClasesDesdeFirestore()
        return view
    }

    private fun cargarClasesDesdeFirestore() {
        db.collection("clases")
            .get()
            .addOnSuccessListener { result ->
                lista_clases.clear()

                if (result.isEmpty) {
                    adapter.notifyDataSetChanged()
                    return@addOnSuccessListener
                }

                for (document in result) {
                    val nombre = document.getString("nombre") ?: "Clase sin nombre"
                    val entrenadorId = document.getString("nombreE") ?: "Sin entrenador"
                    val hora = document.getString("hora") ?: "00:00"
                    val capacidad = document.getLong("capacidad")?.toInt() ?: 20

                    // Consultamos las reservas actuales para esta clase específica
                    db.collection("reservas")
                        .whereEqualTo("nombre_clase", nombre)
                        .get()
                        .addOnSuccessListener { resultReservas ->
                            val inscritos = resultReservas.size()
                            val lugarDisponible = "$inscritos/$capacidad"

                            val clasesGym = ClasesGym(
                                nombreClase = nombre,
                                nombreEntrenador = entrenadorId,
                                horaClase = hora,
                                numeroLugares = lugarDisponible
                            )

                            lista_clases.add(clasesGym)
                            adapter.notifyDataSetChanged()
                        }
                        .addOnFailureListener {
                            val claseGym = ClasesGym(
                                nombreClase = nombre,
                                nombreEntrenador = entrenadorId,
                                horaClase = hora,
                                numeroLugares = "0/$capacidad"
                            )
                            lista_clases.add(claseGym)
                            adapter.notifyDataSetChanged()
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("ClasesFragment", "Error al cargar clases", e)
            }
    }

    private fun realizarReserva(clase: ClasesGym) {
        // Datos que guardaremos en la colección "reservas" de Firestore
        val nuevaReserva = hashMapOf(
            "fechaCreacion" to com.google.firebase.Timestamp.now(),
            "fecha_clases" to com.google.firebase.Timestamp.now(),
            "id_clases" to "",
            "id_miembros" to "",
            "nombre_clase" to clase.nombreClase,
            "hora" to clase.horaClase,
            "entrenador" to clase.nombreEntrenador,
            "estado" to "confirmada"

        )

        // Insertamos el documento en la colección "reservas"
        db.collection("reservas")
            .add(nuevaReserva)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(requireContext(), "¡Reserva exitosa para ${clase.nombreClase}!", Toast.LENGTH_SHORT).show()
                // Opcional: Aquí podrías actualizar el contador visual de lugares si lo deseas
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error al realizar la reserva", Toast.LENGTH_SHORT).show()
                Log.e("ClasesFragment", "Error al guardar reserva", e)
            }
    }
}