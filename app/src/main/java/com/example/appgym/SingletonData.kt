package com.example.appgym

object SingletonData {

    lateinit var  usuario: String
    lateinit var  usuasrioPass: String
    lateinit var arrayList_mensajes: ArrayList<String>
    lateinit var arrayList_validaciones: ArrayList<String>

    init{
        arrayList_mensajes = ArrayList<String>()

        arrayList_mensajes.add(0, "Los campos no pueden estar vacios")
        arrayList_mensajes.add(1, "El formato del correo no es valido")
        arrayList_mensajes.add(2, "Solo usar numero para la contraseña")
        arrayList_mensajes.add(3, "Caracter invalido para el nombre")
        arrayList_mensajes.add(4, "El numero de celular invalido")
        arrayList_mensajes.add(5, "Apellido invalido, usar solo letras")
        arrayList_mensajes.add(6, "No ingrese espacios")
        arrayList_mensajes.add(7, "No se pudo registrar al nuevo miembro")


        arrayList_validaciones = ArrayList<String>()
        arrayList_validaciones.add(0, "^[A-Za-z+_.-]+@[A-Za-z0-9.-]+$") // correo electronico


    }

}