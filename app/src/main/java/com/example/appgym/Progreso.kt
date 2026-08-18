package com.example.appgym

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

// Modelo de datos mapeado directamente con Firestore
data class RegistroProgreso(
    val peso: Double = 0.0,
    val estatura: Double = 0.0,
    val imc: Double = 0.0,
    val grasaCorporal: Double = 0.0,
    val fecha: Timestamp? = null
)

class Progreso : Fragment() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private lateinit var rvHistorial: RecyclerView
    private lateinit var adapter: EntradasAdapter
    private val listaProgreso = mutableListOf<RegistroProgreso>()

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_progreso,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración de la lista (RecyclerView)
        rvHistorial = view.findViewById(R.id.rv_historial)
        rvHistorial.layoutManager = LinearLayoutManager(context)
        adapter = EntradasAdapter(listaProgreso)
        rvHistorial.adapter = adapter

        // Listener del botón de registro
        val boton = view.findViewById<TextView>(R.id.btn_registrar_entrada)
        boton.setOnClickListener {
            mostrarFormulario()
        }

        // Cargar historial en tiempo real
        cargarUltimosRegistros()
    }


    private fun cargarUltimosRegistros() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("progreso")
            .whereEqualTo("uid", userId)
            .orderBy("fecha", Query.Direction.DESCENDING) // Los más recientes primero
            .limit(10) // Límite estricto de 10 registros
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    Log.e("ProgresoFragment", "Error al cargar registros: ${error.message}")
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    listaProgreso.clear()
                    for (doc in querySnapshot.documents) {
                        val registro = doc.toObject(RegistroProgreso::class.java)
                        if (registro != null) {
                            listaProgreso.add(registro)
                        }
                    }
                    // Notifica al adaptador que la lista se actualizó
                    adapter.notifyDataSetChanged()
                }
            }
    }

    private fun mostrarFormulario() {
        val vista = layoutInflater.inflate(R.layout.registrar_entrada, null)

        val txtPeso = vista.findViewById<EditText>(R.id.txtPeso)
        val txtEstatura = vista.findViewById<EditText>(R.id.txtEstatura)
        val txtGrasa = vista.findViewById<EditText>(R.id.txtGrasa)
        val txtFecha = vista.findViewById<TextView>(R.id.txtFecha)
        val btnRegistrar = vista.findViewById<Button>(R.id.btnRegistrar)

        // 1. Asignar fecha actual automáticamente
        val fechaActual = SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH).format(Date())
        txtFecha.text = fechaActual

        // Bloquear edición manual en el porcentaje de grasa
        txtGrasa.isFocusable = false
        txtGrasa.isClickable = false

        // 2. Cálculo automático en tiempo real
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calcularGrasaAutomatico(txtPeso, txtEstatura, txtGrasa)
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        txtPeso.addTextChangedListener(watcher)
        txtEstatura.addTextChangedListener(watcher)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(vista)
            .create()

        // Boton de cerrar (X)
        vista.findViewById<ImageView>(R.id.btn_cerrar_registrar)
            .setOnClickListener {
                dialog.dismiss()
            }

        // 3. Envío del nuevo registro a Firestore
        btnRegistrar.setOnClickListener {
            val pesoStr = txtPeso.text.toString()
            val estaturaStr = txtEstatura.text.toString()
            val grasaStr = txtGrasa.text.toString()
            val userId = auth.currentUser?.uid ?: return@setOnClickListener

            if (pesoStr.isEmpty() || estaturaStr.isEmpty()) {
                Toast.makeText(context, "Por favor ingresa peso y estatura", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val peso = pesoStr.toDoubleOrNull() ?: 0.0
            val estatura = estaturaStr.toDoubleOrNull() ?: 0.0
            val grasa = grasaStr.toDoubleOrNull() ?: 0.0
            val imc = if (estatura > 0) peso / (estatura * estatura) else 0.0

            val nuevoRegistro = hashMapOf(
                "uid" to userId,
                "peso" to peso,
                "estatura" to estatura,
                "grasaCorporal" to grasa,
                "imc" to String.format(Locale.US, "%.2f", imc).toDouble(),
                "fecha" to FieldValue.serverTimestamp()
            )

            db.collection("progreso")
                .add(nuevoRegistro)
                .addOnSuccessListener {
                    Toast.makeText(context, "Registro guardado correctamente", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        dialog.show()
    }

    private fun calcularGrasaAutomatico(txtPeso: EditText, txtEstatura: EditText, txtGrasa: EditText) {
        val peso = txtPeso.text.toString().toDoubleOrNull()
        val estatura = txtEstatura.text.toString().toDoubleOrNull()

        if (peso != null && estatura != null && estatura > 0) {
            val imc = peso / (estatura * estatura)
            val grasaEstimada = (1.20 * imc) - 5.4
            val porcentajeFinal = if (grasaEstimada > 0) grasaEstimada else 0.0

            txtGrasa.setText(String.format(Locale.US, "%.1f", porcentajeFinal))
        } else {
            txtGrasa.setText("")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Progreso().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

    }

}