package com.example.appgym

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
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

        val vistaFechas =
            view.findViewById<View>(R.id.vista_fechas)


        val btnVerMembresias =
            view.findViewById<View>(R.id.btn_ver_membresias)

        // Abrir historial en Dialog
        btnVerMembresias.setOnClickListener {

            val dialog = Dialog(requireContext())

            dialog.requestWindowFeature(
                Window.FEATURE_NO_TITLE
            )

            val vistaHistorial = layoutInflater.inflate(
                R.layout.historial_membresia,
                null
            )

            dialog.setContentView(vistaHistorial)

            dialog.window?.setBackgroundDrawableResource(
                android.R.color.transparent
            )

            dialog.show()

            dialog.window?.setLayout(
                (resources.displayMetrics.widthPixels * 0.95).toInt(),
                (resources.displayMetrics.heightPixels * 0.90).toInt()
            )
        }

        // Tarjeta 1
        configurarTarjetaExpandible(
            view,
            R.id.header_plan1,
            R.id.body_plan1,
            R.id.icono_flecha_plan1
        )

        // Tarjeta 2
        configurarTarjetaExpandible(
            view,
            R.id.header_plan2,
            R.id.body_plan2,
            R.id.icono_flecha_plan2
        )

        // Tarjeta 3
        configurarTarjetaExpandible(
            view,
            R.id.header_plan3,
            R.id.body_plan3,
            R.id.icono_flecha_plan3
        )
    }

    private fun configurarTarjetaExpandible(
        view: View,
        headerId: Int,
        bodyId: Int,
        flechaId: Int
    ) {

        val header =
            view.findViewById<View>(headerId)

        val body =
            view.findViewById<View>(bodyId)

        val flecha =
            view.findViewById<View>(flechaId)

        header.setOnClickListener {

            val estaVisible =
                body.visibility == View.VISIBLE

            if (estaVisible) {

                body.visibility = View.GONE
                flecha.rotation = 0f

            } else {

                body.visibility = View.VISIBLE
                flecha.rotation = 180f
            }
        }
    }
}