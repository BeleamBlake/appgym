package com.example.appgym

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class RecuperarPassword : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()

        val etEmail = findViewById<EditText>(R.id.et_email_recovery)
        val btnRecover = findViewById<Button>(R.id.btn_recover_password)

        btnRecover.setOnClickListener {
            val email = etEmail.text.toString().trim()

            if (email.isEmpty()) {
                etEmail.error = "Ingresa tu correo electrónico"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            // Enviar enlace de restablecimiento de contraseña
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Se ha enviado un correo para restablecer tu contraseña",
                            Toast.LENGTH_LONG
                        ).show()
                        finish() // Cierra la pantalla al completarse
                    } else {
                        val error = task.exception?.localizedMessage ?: "Error desconocido"
                        Toast.makeText(
                            this,
                            "Error",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        val bt_regresar = findViewById<ImageButton>(R.id.btRegresar)
        bt_regresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }




    }
}