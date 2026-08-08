package com.example.appgym

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity3 : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main3)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val bt_nuevo: Button = findViewById<Button>(R.id.nv_miembro)
        val nombre = findViewById<EditText>(R.id.for_nom)
        val ap = findViewById<EditText>(R.id.for_ap)
        val cel = findViewById<EditText>(R.id.for_celular)
        val email = findViewById<EditText>(R.id.for_correo)
        val password = findViewById<EditText>(R.id.for_pass)

        bt_nuevo.setOnClickListener {

            val correoRegex = Regex("^[A-Za-z+_.-]+@[A-Za-z0-9.-]+$")
            val cellRegex = Regex("[0-9]{10}")
            val passRegex = Regex("[0-9]{6,}$")
            val nomRegex = Regex("[A-Za-záeéiíoóuúüñAÁEÉIÍOÓUÚÜÑ]+$")
            val apRegex = Regex("[A-Za-záeéiíoóuúüñAÁEÉIÍOÓUÚÜÑ]+$")
            val nom = nombre.text.toString().trim()
            val ape = ap.text.toString().trim()
            val celu = cel.text.toString().trim()
            val corr = email.text.toString().trim()
            val pass = password.text.toString().trim()

            val correoValido = correoRegex.matches(corr)
            val celValido = cellRegex.matches(celu)
            val nombreValido = nomRegex.matches(nom)
            val apellidoValido = apRegex.matches(ape)
            val passValido = passRegex.matches(pass)

            // aqui acemos validaciones por si los campos estan vacios
            if (nom.isEmpty() || ape.isEmpty() || celu.isEmpty() || corr.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Los campos no puden estar vacios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //aqui valida el fomato del correo
            if (!correoValido) {
                Toast.makeText(
                    this@MainActivity3,
                    "Correo invalido",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!passValido) {
                Toast.makeText(
                    this@MainActivity3,
                    "Contraseña invalida",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!nombreValido) {
                Toast.makeText(
                    this@MainActivity3,
                    "Nombre invalido",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!celValido) {
                Toast.makeText(
                    this@MainActivity3,
                    "Numero de Celular invalido",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (!apellidoValido) {
                Toast.makeText(
                    this@MainActivity3,
                    "Apellido invalido",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(corr, pass)
                .addOnCompleteListener { tareaAuth ->
                    if (tareaAuth.isSuccessful) {
                        //aqui_asigna el_id unico que nos da la base de datos
                        val idmiembro: String = tareaAuth.result?.user?.uid.toString()

                        val usuariomap = hashMapOf(
                            "nombre" to nom,
                            "apellido" to ape,
                            "celular" to celu,
                            "correo" to corr,
                            "password" to pass
                        )
                        // 4. Guardar en la colección "Miembros" usando el userId como nombre del documento
                        db.collection("miembros").document(idmiembro)
                            .set(usuariomap)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this@MainActivity3,
                                    "Miembro registrado con exito",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this@MainActivity3, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    this@MainActivity3,
                                    "Error en la coneccion con la Base de datos, ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        Toast.makeText(
                            this@MainActivity3,
                            "No se pudo registrar al nuevo miembro",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}


