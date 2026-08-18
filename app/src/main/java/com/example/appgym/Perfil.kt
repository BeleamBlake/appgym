package com.example.appgym


import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
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

        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val tvNombre = view.findViewById<TextView>(R.id.txt_nombre_miembro)
        val tvCorreo = view.findViewById<TextView>(R.id.txt_correo)
        val tvFechaNacimiento = view.findViewById<TextView>(R.id.txt_fecha_nacimiento)
        val tvContacto = view.findViewById<TextView>(R.id.txt_contacto)
        val tvMembresia = view.findViewById<TextView>(R.id.txt_num_membresia)
        val tvEstadoMembresia = view.findViewById<TextView>(R.id.txt_estado)


        val userId = auth.currentUser?.uid ?: return

        db.collection("miembros")
            .whereEqualTo("uid", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]


                    val nombre = document.getString("nombre") ?: ""
                    val apellido = document.getString("apellido") ?: ""
                    tvNombre.text = "$nombre $apellido".trim()


                    tvCorreo.text = document.getString("correo") ?: auth.currentUser?.email


                    tvFechaNacimiento.text = document.getString("fechaNacimiento") ?: "Sin fecha"


                    tvContacto.text = document.getString("celular") ?: "Sin número"


                    val numMembresia = document.get("numeroMembresia")?.toString()?:"sin numero"
                    tvMembresia.text = numMembresia


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

        // cerrar sesion
        val btnCerrarSesion = view.findViewById<LinearLayout>(R.id.btn_cerrar_sesion)
        btnCerrarSesion.setOnClickListener {
            auth.signOut()

            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        val btnMisReservas = view.findViewById<LinearLayout>(R.id.btn_mis_reservas)

        btnMisReservas.setOnClickListener {

            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

            val vistaReservas = layoutInflater.inflate(
                R.layout.dialog_mis_reservas,
                null
            )

            dialog.setContentView(vistaReservas)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()

            dialog.window?.setLayout(
                (resources.displayMetrics.widthPixels * 0.95).toInt(),
                (resources.displayMetrics.heightPixels * 0.90).toInt()
            )

            // Boton de cerrar
            vistaReservas.findViewById<ImageView>(R.id.btn_cerrar_reservas)
                .setOnClickListener {
                    dialog.dismiss()
                }

            // agregar las clases reservadas, una por una
            val contenedor = vistaReservas.findViewById<LinearLayout>(R.id.contenedor_reservas)

            // TODO: reemplazar esta lista fija por las reservas reales del usuario
            val misReservas = listOf(
                "Yoga - Lunes 08:00",
                "Spinning - Miercoles 09:30"
            )

            if (misReservas.isEmpty()) {
                val txtVacio = TextView(requireContext())
                txtVacio.text = "No tienes reservas activas"
                txtVacio.setTextColor(resources.getColor(R.color.text_primary, null))
                contenedor.addView(txtVacio)
            } else {
                for (reserva in misReservas) {
                    val txt = TextView(requireContext())
                    txt.text = "• $reserva"
                    txt.textSize = 15f
                    txt.setTextColor(resources.getColor(R.color.text_primary, null))
                    txt.setPadding(0, 12, 0, 12)
                    contenedor.addView(txt)
                }
            }
        }

    }
}