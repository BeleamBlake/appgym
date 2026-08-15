package com.example.appgym

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class Bloqueo : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el diseño XML de tu pantalla de bloqueo
        return inflater.inflate(R.layout.fragment_bloqueo, container, false)
    }

}