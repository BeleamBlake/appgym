package com.example.appgym

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class Membresia : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_membresia, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vistaFechas = view.findViewById<View>(R.id.vista_fechas)
        val vistaTarjetas = view.findViewById<View>(R.id.vista_tarjetas)
        val btnVerMembresias = view.findViewById<View>(R.id.btn_ver_membresias)

        // Al tocar "Ver Membresias": se oculta la vista de fechas
        // y aparece el acordeon de tarjetas, todo dentro del mismo Fragment.
        btnVerMembresias.setOnClickListener {
            vistaFechas.visibility = View.GONE
            vistaTarjetas.visibility = View.VISIBLE
        }

        // Conectamos cada header con su body del acordeon
        configurarTarjetaExpandible(
            view,
            headerId = R.id.header_plan1,
            bodyId = R.id.body_plan1,
            flechaId = R.id.icono_flecha_plan1
        )

        configurarTarjetaExpandible(
            view,
            headerId = R.id.header_plan2,
            bodyId = R.id.body_plan2,
            flechaId = R.id.icono_flecha_plan2
        )

        configurarTarjetaExpandible(
            view,
            headerId = R.id.header_plan3,
            bodyId = R.id.body_plan3,
            flechaId = R.id.icono_flecha_plan3
        )
    }

    private fun configurarTarjetaExpandible(
        view: View,
        headerId: Int,
        bodyId: Int,
        flechaId: Int
    ) {
        val header = view.findViewById<View>(headerId)
        val body = view.findViewById<View>(bodyId)
        val flecha = view.findViewById<View>(flechaId)

        header.setOnClickListener {
            val estaVisible = body.visibility == View.VISIBLE

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