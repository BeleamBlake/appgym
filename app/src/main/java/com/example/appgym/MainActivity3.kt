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
        val btvolver = findViewById<Button>(R.id.btnvolver)

        btvolver.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
            finish()
        }

       /* bt_nuevo.setOnClickListener {
            val correoRegex = Regex(SingletonData.arrayList_validaciones[0])
            val nom = nombre.text.toString().trim()
            val  ape = ap.text.toString().trim()
            val celu = cel.text.toString().trim()
            val corr = email.text.toString().trim()
            val pass = password.text.toString().trim()

            val correoValido = correoRegex.matches(corr)

            // aqui acemos validaciones por si los campos estan vacios
            if (nom.isEmpty() || ape.isEmpty() || celu.isEmpty() || corr.isEmpty() || pass.isEmpty() ){
                Toast.makeText(this, SingletonData.arrayList_mensajes[0], Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //aqui valida el fomato del correo
            if (!correoValido){
                Toast.makeText(this, SingletonData.arrayList_mensajes[1], Toast.LENGTH_SHORT).show()
            }

            auth.createUserWithEmailAndPassword(corr, pass)
                .addOnCompleteListener { tareaAuth ->
                    if (tareaAuth.isSuccessful){

                        //aqui asigna el id unico que nos da la base de datos
                        //val idmiembro = auth.currentUser?.uid.toString()

                        val idmiembro: String = tareaAuth.result?.user?.uid.toString()

                        val usuariomap = hashMapOf(
                            "nombre" to nom,
                            "apellido" to ape,
                            "celular" to celu,
                            "correo" to corr,
                            "password" to pass
                        )
                        // 4. Guardar en la colección "Miembros" usando el userId como nombre del documento
                        if (usuariomap != null) {
                            db.collection("miembros").document(idmiembro)
                                .set(usuariomap)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Miembro registrado con éxito", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Error al guardar datos: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Error en registro: ${tareaAuth.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }*/

    }

}