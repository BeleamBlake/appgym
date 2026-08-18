package com.example.appgym

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Membresia : Fragment() {

    private lateinit var txtEstadoMembresia: TextView
    private lateinit var txtMiembroDesde: TextView
    private lateinit var txtInicioPlan: TextView
    private lateinit var txtProximaRenovacion: TextView
    private lateinit var txtPlanActual: TextView
    private lateinit var txtBeneficiosPlan: TextView

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_membresia,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        // FIND BY ID

        txtEstadoMembresia =
            view.findViewById(R.id.txt_estado_membresia)

        txtMiembroDesde =
            view.findViewById(R.id.txt_miembro_desde)

        txtInicioPlan =
            view.findViewById(R.id.txt_inicio_plan)

        txtProximaRenovacion =
            view.findViewById(R.id.txt_proxima_renovacion)

        txtPlanActual =
            view.findViewById(R.id.txt_plan_actual)

        txtBeneficiosPlan =
            view.findViewById(R.id.beneficios_plan)

        cargarMembresia()
    }

    private fun cargarMembresia() {

        val uid = auth.currentUser?.uid

        if (uid == null) {

            Toast.makeText(
                requireContext(),
                "No hay un usuario autenticado",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        // ==========================================
        // BUSCAR USUARIO
        // ==========================================

        db.collection("usuarios")
            .document(uid)
            .get()
            .addOnSuccessListener { usuario ->

                if (!usuario.exists()) {

                    Toast.makeText(
                        requireContext(),
                        "No existe el perfil del usuario",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@addOnSuccessListener
                }

                val memberId =
                    usuario.getString("memberId")

                if (memberId.isNullOrEmpty()) {

                    Toast.makeText(
                        requireContext(),
                        "No se encontró el memberId",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@addOnSuccessListener
                }

                // ==========================================
                // BUSCAR MIEMBRO
                // ==========================================

                db.collection("miembros")
                    .document(memberId)
                    .get()
                    .addOnSuccessListener { miembro ->

                        if (!miembro.exists()) {

                            Toast.makeText(
                                requireContext(),
                                "No existe el registro del miembro",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@addOnSuccessListener
                        }

                        // ==========================================
                        // ESTADO
                        // ==========================================

                        val estado =
                            miembro.getString("estado")
                                ?: "SIN ESTADO"

                        txtEstadoMembresia.text = estado

                        // ==========================================
                        // MIEMBRO DESDE
                        // Campo correcto: creadoEn
                        // ==========================================

                        val creadoEn =
                            miembro.get("creadoEn")

                        txtMiembroDesde.text =
                            convertirFecha(creadoEn)

                        // ==========================================
                        // INICIO DEL PLAN
                        // ==========================================

                        val fechaInicio =
                            miembro.get("fechaInicioMembresia")

                        txtInicioPlan.text =
                            convertirFecha(fechaInicio)

                        // ==========================================
                        // PRÓXIMA RENOVACIÓN
                        // ==========================================

                        val fechaVencimiento =
                            miembro.get("fechaVencimientoMembresia")

                        txtProximaRenovacion.text =
                            convertirFecha(fechaVencimiento)

                        // ==========================================
                        // PLAN ACTUAL
                        // ==========================================

                        val planId =
                            miembro.getString("planId")

                        if (planId.isNullOrEmpty()) {

                            txtPlanActual.text =
                                "Sin plan"

                            txtBeneficiosPlan.text =
                                "No hay beneficios registrados"

                            return@addOnSuccessListener
                        }

                        cargarPlan(planId)
                    }
                    .addOnFailureListener { error ->

                        Log.e(
                            "Membresia",
                            "Error obteniendo miembro",
                            error
                        )

                        Toast.makeText(
                            requireContext(),
                            "Error al obtener la membresía",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
            .addOnFailureListener { error ->

                Log.e(
                    "Membresia",
                    "Error obteniendo usuario",
                    error
                )

                Toast.makeText(
                    requireContext(),
                    "Error al obtener el usuario",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    // ==========================================
    // CARGAR PLAN
    // ==========================================

    private fun cargarPlan(planId: String) {

        db.collection("planes")
            .document(planId)
            .get()
            .addOnSuccessListener { plan ->

                if (!plan.exists()) {

                    txtPlanActual.text = "Plan no encontrado"
                    txtBeneficiosPlan.text = "No hay información disponible"

                    return@addOnSuccessListener
                }

                // ==============================
                // NOMBRE DEL PLAN
                // ==============================

                val nombrePlan =
                    plan.getString("nombre") ?: "Sin nombre"

                txtPlanActual.text = nombrePlan


                // ==============================
                // BENEFICIOS DEL PLAN
                // ==============================

                val beneficios =
                    plan.getString("descripcion")

                if (!beneficios.isNullOrEmpty()) {

                    // Cambia las comas por saltos de línea
                    txtBeneficiosPlan.text =
                        beneficios.replace(",", "\n• ")

                } else {

                    txtBeneficiosPlan.text =
                        "No hay beneficios registrados"
                }
            }
            .addOnFailureListener { error ->

                Log.e(
                    "Membresia",
                    "Error obteniendo plan",
                    error
                )

                txtPlanActual.text = "Error"

                txtBeneficiosPlan.text =
                    "No se pudieron cargar los beneficios"
            }
    }
    // ==========================================
    // CONVERTIR FECHA DE FIREBASE
    // ==========================================

    private fun convertirFecha(valor: Any?): String {

        if (valor == null) {
            return "No disponible"
        }

        val fecha: Date? = when (valor) {

            is Timestamp -> {
                valor.toDate()
            }

            is Date -> {
                valor
            }

            else -> {
                null
            }
        }

        if (fecha == null) {
            return "No disponible"
        }

        val formato =
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            )

        return formato.format(fecha)
    }
}