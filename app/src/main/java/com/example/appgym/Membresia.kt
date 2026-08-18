package com.example.appgym

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.text.SimpleDateFormat
import java.util.Locale
import android.graphics.Color

class Membresia : Fragment() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private var listenerRegistration: ListenerRegistration? = null

    // Vistas para los campos de datos
    private lateinit var tvEstadoMembresia: TextView
    private lateinit var tvMiembroDesde: TextView
    private lateinit var tvInicioPlan: TextView
    private lateinit var tvProximaRenovacion: TextView
    private lateinit var tvNombrePlanActual: TextView
    private lateinit var tvBeneficiosPlan: TextView

    // 1. Declarar la variable para tu botón
    private lateinit var btnVerMembresias: TextView // O Button, dependiendo de qué usaste en tu XML

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_membresia, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vincular la vista obligatoriamente antes de la llamada a la BD
        tvEstadoMembresia = view.findViewById(R.id.txt_estado_membresia)

        // Vincular Vistas de las fechas
        tvMiembroDesde = view.findViewById(R.id.txt_miembro_desde)
        tvInicioPlan = view.findViewById(R.id.txt_inicio_plan)
        tvProximaRenovacion = view.findViewById(R.id.txt_proxima_renovacion)

        // 2. Vincular el botón con su ID del XML
        // Asegúrate de que el ID "btnVerMembresias" coincida exactamente con el de tu fragment_membresia.xml
        btnVerMembresias = view.findViewById(R.id.btn_ver_membresias)

        // 3. Configurar el evento de clic del botón
        btnVerMembresias.setOnClickListener {

        }

        // Cargar el estado
        obtenerEstadoMembresia()

        // Cargar fechas
        cargarFechasMembresia()
    }

    private fun obtenerEstadoMembresia() {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            tvEstadoMembresia.text = "SIN SESIÓN"
            tvEstadoMembresia.setTextColor(Color.YELLOW)
            return
        }

        // Consulta buscando el documento donde el campo 'uid' coincida con el usuario actual
        listenerRegistration = db.collection("miembros")
            .whereEqualTo("uid", userId)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null || querySnapshot == null || querySnapshot.isEmpty) {
                    Log.e("MembresiaDebug", "No se encontró documento para el UID: $userId")
                    tvEstadoMembresia.text = "DESCONOCIDO"
                    tvEstadoMembresia.setTextColor(Color.YELLOW)
                    return@addSnapshotListener
                }

                // Tomamos el primer documento que coincida
                val document = querySnapshot.documents[0]

                val estado = document.getString("estado") ?: "Inactiva"
                tvEstadoMembresia.text = estado.uppercase()

                if (estado.equals("activa", ignoreCase = true)) {
                    tvEstadoMembresia.setTextColor(Color.GREEN)
                } else {
                    tvEstadoMembresia.setTextColor(Color.YELLOW)
                }
            }
    }

    private fun cargarFechasMembresia() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("miembros")
            .whereEqualTo("uid", userId)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null || querySnapshot == null || querySnapshot.isEmpty) {
                    tvMiembroDesde.text = "---"
                    tvInicioPlan.text = "---"
                    tvProximaRenovacion.text = "---"
                    return@addSnapshotListener
                }

                val document = querySnapshot.documents[0]
                val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                // Extracción de Timestamps de Firestore
                val creadoEn = document.getTimestamp("creadoEn")
                val fechaInicio = document.getTimestamp("fechaInicioMembresia")
                val fechaVencimiento = document.getTimestamp("fechaVencimientoMembresia")

                // Asignación a las vistas de texto
                tvMiembroDesde.text = formatearFecha(creadoEn, formatoFecha)
                tvInicioPlan.text = formatearFecha(fechaInicio, formatoFecha)
                tvProximaRenovacion.text = formatearFecha(fechaVencimiento, formatoFecha)
            }
    }

    // Función auxiliar para formatear Timestamps de forma segura
    private fun formatearFecha(timestamp: Timestamp?, formato: SimpleDateFormat): String {
        return if (timestamp != null) {
            formato.format(timestamp.toDate())
        } else {
            "---"
        }
    }
}