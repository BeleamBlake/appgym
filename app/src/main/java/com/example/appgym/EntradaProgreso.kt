package com.example.appgym

import com.google.firebase.Timestamp

data class EntradaProgreso(
    val id: String = "",
    val peso: Double = 0.0,
    val estatura: Double = 0.0,
    val grasaCorporal: Double = 0.0,
    val fecha: Timestamp? = null
)