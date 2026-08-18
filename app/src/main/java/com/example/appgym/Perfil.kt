package com.example.appgym
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

                    val numMembresia = document.get("numeroMembresia")?.toString() ?: "sin numero"
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

        // Botón Cerrar Sesión
        val btnCerrarSesion = view.findViewById<LinearLayout>(R.id.btn_cerrar_sesion)
        btnCerrarSesion.setOnClickListener {
            auth.signOut()

            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        // Botón Mis Reservas sincronizado con "realizarReserva"
        val btnMisReservas = view.findViewById<LinearLayout>(R.id.btn_mis_reservas)
        btnMisReservas.setOnClickListener {

            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

            val vistaReservas = layoutInflater.inflate(
                R.layout.listasreservas,
                null
            )

            dialog.setContentView(vistaReservas)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()

            dialog.window?.setLayout(
                (resources.displayMetrics.widthPixels * 0.95).toInt(),
                (resources.displayMetrics.heightPixels * 0.90).toInt()
            )

            // Botón de cerrar del diálogo
            vistaReservas.findViewById<ImageView>(R.id.btn_cerrar_reservas)
                .setOnClickListener {
                    dialog.dismiss()
                }

            // Configuración del RecyclerView usando el ID (reservalista) de listasreservas.xml
            val rvMisReservas = vistaReservas.findViewById<RecyclerView>(R.id.reservalista)
            rvMisReservas.layoutManager = LinearLayoutManager(requireContext())

            val listaReservas = mutableListOf<ClasesGym>()
            val adaptadorReservas = ClasesAdapter(listaReservas) { _ ->
                // Acción opcional al hacer clic en una tarjeta de reserva
            }
            rvMisReservas.adapter = adaptadorReservas

            val currentUserId = auth.currentUser?.uid

            if (currentUserId != null) {
                // Consultamos la colección "reservas" filtrando por "id_miembros"
                // para que coincida exactamente con lo que guardas en realizarReserva
                db.collection("reservas")
                    .whereEqualTo("id_miembros", currentUserId)
                    .get()
                    .addOnSuccessListener { documents ->
                        listaReservas.clear()
                        for (document in documents) {
                            val nombreClase = document.getString("nombre_clase") ?: "Clase"
                            val horaClase = document.getString("hora") ?: "00:00"
                            val entrenador = document.getString("entrenador") ?: ""

                            val reservaItem = ClasesGym(
                                nombreClase = nombreClase,
                                nombreEntrenador = entrenador,
                                horaClase = horaClase,
                                numeroLugares = ""
                            )
                            listaReservas.add(reservaItem)
                        }
                        adaptadorReservas.notifyDataSetChanged()
                    }
                    .addOnFailureListener { e ->
                        Log.e("PerfilFragment", "Error al cargar reservas", e)
                        Toast.makeText(context, "Error al cargar reservas", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}