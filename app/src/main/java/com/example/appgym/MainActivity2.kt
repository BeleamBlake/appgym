package com.example.appgym

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar Firebase PRIMERO, antes de usarlo
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val userId = auth.currentUser?.uid
        if (userId == null) {
            // No hay sesion iniciada, no seguimos
            return
        }

        val menuBajo = findViewById<BottomNavigationView>(R.id.menuBajo)
        val ventanaBloqueo = findViewById<LinearLayout>(R.id.ventanaInicial)

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

        // Fragment que aparece al iniciar
        cargarFragment(home())

        db.collection("miembros").document(userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener

                val estadoMembresia = snapshot.getString("estado") // "activa", "inactiva", "vencida"

                if (estadoMembresia == "inactiva" || estadoMembresia == "vencida") {
                    ventanaBloqueo.visibility = View.VISIBLE
                    menuBajo.visibility = View.GONE
                } else {
                    ventanaBloqueo.visibility = View.GONE
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