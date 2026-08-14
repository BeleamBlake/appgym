package com.example.appgym

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    // esta variable es para que puedar comparar los campos de inicio
    // con la base de datos
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()

        val username = findViewById<EditText>(R.id.user_login)
        val userpass = findViewById<EditText>(R.id.pass_login)
        val bt_entrar = findViewById<Button>(R.id.bt_inicio)

        bt_entrar.setOnClickListener {
            val correo = username.text.toString().trim()
            val contrasena = userpass.text.toString().trim()

            //Validamos que los campos no esten vacios

            if (correo.isEmpty() || contrasena.isEmpty()){
                Toast.makeText(this, "No puede estar campos vacíos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //aqui inicia sesion en firebase
            auth.signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener { tareaAuth->
                    if (tareaAuth.isSuccessful){
                        Toast.makeText(this, "Bienvenido sea a ExoFit Elite", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, MainActivity2::class.java)
                        startActivity(intent)
                        finish() // este metodo funciona para que se cierre el login y no puede regresar
                    }else{
                        Toast.makeText(this, "Error: ${tareaAuth.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

        }


//        val boton_registro = findViewById<Button>(R.id.bt_registro)
//        boton_registro.setOnClickListener {
//            val intent = Intent(this, MainActivity3::class.java)
//            startActivity(intent)
//        }

        val btRecuperar = findViewById<Button>(R.id.bt_recuperar)
        btRecuperar.setOnClickListener {
        val intent = Intent(this, RecuperarPassword::class.java)
        startActivity(intent)
        }
        fun onStart() {
            super.onStart()

            // 2. Obtener el usuario actual (Firebase lee automáticamente el token local)
            val usuarioActual = auth.currentUser

            // 3. Evaluar si existe una sesión activa almacenada
            if (usuarioActual != null) {
                // EL TOKEN EXISTE Y ES VÁLIDO: Ir directamente a la pantalla principal
                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
                finish() // Cierra el Splash para que el usuario no vuelva atrás con el botón de regresar
            } else {
                // NO HAY TOKEN O EXPIRÓ: Enviar a iniciar sesión
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }
}