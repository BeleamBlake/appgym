package com.example.appgym

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Progreso : Fragment() {

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

    // AQUÍ DETECTAMOS EL CLICK
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val boton = view.findViewById<TextView>(
            R.id.btn_registrar_entrada
        )

        boton.setOnClickListener {
            mostrarFormulario()
        }
    }

    private fun mostrarFormulario() {

        val vista = layoutInflater.inflate(
            R.layout.registrar_entrada,
            null
        )

        val dialog = AlertDialog.Builder(requireContext())
            .setView(vista)
            .create()

        dialog.show()
    }

    companion object {

        @JvmStatic
        fun newInstance(
            param1: String,
            param2: String
        ) =
            Progreso().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}