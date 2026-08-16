package com.example.appgym

import android.R.attr.fragment
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity2 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var ventanaInicial: LinearLayout
    private lateinit var menuBajo: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        // Inicializar Firebase PRIMERO
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        menuBajo = findViewById(R.id.menuBajo)

        // Verificar usuario
        val currentUser = auth.currentUser

        if (currentUser == null) {
            Toast.makeText(
                this,
                "No hay sesión activa",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val userId = currentUser.uid

        // Menú inferior
        menuBajo.setOnItemSelectedListener { item ->

            val fragment: Fragment = when (item.itemId) {

                R.id.item_home -> home()

                R.id.item_perfil -> Perfil()

                R.id.item_progreso -> Progreso()

                R.id.item_clases -> Clases()

                R.id.item_membresia -> Membresia()

                else -> home()
            }

            cargarFragment(fragment)

            true
        }

        // Escuchar estado de membresía
        db.collection("miembros")
            .document(userId)
            .addSnapshotListener { snapshot, e ->

                if (e != null) {
                    Toast.makeText(
                        this,
                        "Error al consultar membresía",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@addSnapshotListener
                }

                if (snapshot == null || !snapshot.exists()) {
                    return@addSnapshotListener
                }

                val estadoMembresia = snapshot.getString("estado")

                if (estadoMembresia == "inactiva" ||
                    estadoMembresia == "vencida"
                ) {

                    // Mostrar pantalla de bloqueo
                    cargarFragment(Bloqueo())

                    // Ocultar menú
                    menuBajo.visibility = View.GONE

                } else {

                    // Mostrar Home
                    cargarFragment(home())

                    // Mostrar menú
                    menuBajo.visibility = View.VISIBLE
                }
            }
    }

    private fun cargarFragment(fragment: Fragment) {

        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedor_fragment, fragment)
            .commit()
    }
}