package com.example.appgym

import com.google.firebase.firestore.PropertyName

data class EntradaProgreso(
    var estatura: Double = 0.0,
    var peso: Double = 0.0,
    @get:PropertyName("porcentaje_grasa")
    @set:PropertyName("porcentaje_grasa")
    var porcentajeGrasa: Double = 0.0,
    var imc: Double = 0.0,
    var fecha: com.google.firebase.Timestamp? = null
)




/*
import com.google.firebase.Timestamp

data class EntradaProgreso(
    val id: String = "",
    val peso: Double = 0.0,
    val estatura: Double = 0.0,
    val grasaCorporal: Double = 0.0,
    val fecha: Timestamp? = null
)*/