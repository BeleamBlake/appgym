package com.example.appgym

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class Membresia : Fragment() {

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

        // Abrir historial en Dialog
        val btnVerMembresias =
            view.findViewById<TextView>(R.id.btn_ver_membresias)

        btnVerMembresias.setOnClickListener {

            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

            val vistaHistorial = layoutInflater.inflate(
                R.layout.historial_membresia,
                null
            )

            dialog.setContentView(vistaHistorial)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()

            dialog.window?.setLayout(
                (resources.displayMetrics.widthPixels * 0.95).toInt(),
                (resources.displayMetrics.heightPixels * 0.90).toInt()
            )

            // conectar el boton de cerrar del dialog
            vistaHistorial.findViewById<ImageView>(R.id.btn_cerrar_historial)
                .setOnClickListener {
                    dialog.dismiss()
                }
        }
    }
}