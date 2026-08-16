package com.example.appgym

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.appgym.R
import com.google.firebase.auth.FirebaseAuth

class MembresiaBloqueadaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bloqueo, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Asigna el ID exacto que le diste a tu botón en el XML (ejemplo: btnVolver)
        val btnVolver: Button = view.findViewById(R.id.btnvolver)

        btnVolver.setOnClickListener {
            // 1. Cerrar sesión en Firebase Auth
            FirebaseAuth.getInstance().signOut()

            // 2. Redirigir a la pantalla de Login (MainActivity3) y limpiar el historial
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            // 3. Finalizar la Activity contenedora
            requireActivity().finish()
        }
    }
}