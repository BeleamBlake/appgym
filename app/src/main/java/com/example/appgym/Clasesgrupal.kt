package com.example.appgym

data class ClaseGrupal(
    val nombre: String,
    val instructor: String,
    val horario: String,
    val sala: String,
    val lugaresOcupados: Int,
    val lugaresTotales: Int,
    val tipo: String   // "yoga", "spinning", "funcional", "pilates"
)