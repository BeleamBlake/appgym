package com.example.appgym


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Perfil.newInstance] factory method to
 * create an instance of this fragment.
 */
class Perfil : Fragment() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el layout tradicionalmente
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Referencias a los TextViews usando findViewById con la vista recibida
        val tvNombre = view.findViewById<TextView>(R.id.txt_nombre_miembro)
        val tvCorreo = view.findViewById<TextView>(R.id.txt_correo)
        val tvFechaNacimiento = view.findViewById<TextView>(R.id.txt_fecha_nacimiento)
        val tvContacto = view.findViewById<TextView>(R.id.txt_contacto)
        val tvMembresia = view.findViewById<TextView>(R.id.txt_num_membresia)
        val tvEstadoMembresia = view.findViewById<TextView>(R.id.txt_estado)

        // Obtener el ID del usuario con sesión activa
        val userId = auth.currentUser?.uid ?: return

        db.collection("miembros")
            .whereEqualTo("uid", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]

                    // 1. Nombre completo (nombre + apellido)
                    val nombre = document.getString("nombre") ?: ""
                    val apellido = document.getString("apellido") ?: ""
                    tvNombre.text = "$nombre $apellido".trim()

                    // 2. Correo electrónico
                    tvCorreo.text = document.getString("correo") ?: auth.currentUser?.email

                    // 3. Fecha de nacimiento
                    tvFechaNacimiento.text = document.getString("fechaNacimiento") ?: "Sin fecha"

                    // 4. Contacto (mapeado desde el campo 'celular' de Firestore)
                    tvContacto.text = document.getString("celular") ?: "Sin número"

                    // 5. Número de membresía (es un número en Firestore, se convierte a Long/String)
                    val numMembresia = document.getLong("numeroMembresia")
                    tvMembresia.text = numMembresia?.toString() ?: "0"

                    // 6. Estado de la membresía (Extracción y asignación de color)
                    val estado = document.getString("estado") ?: "Desconocido"
                    tvEstadoMembresia.text = estado.uppercase()

                    if (estado.equals("activa", ignoreCase = true)) {
                        tvEstadoMembresia.setTextColor(Color.GREEN)
                    } else {
                        tvEstadoMembresia.setTextColor(Color.RED)
                    }

                } else {
                    Toast.makeText(
                        context,
                        "No se encontró el registro en la base de datos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }

    }
}