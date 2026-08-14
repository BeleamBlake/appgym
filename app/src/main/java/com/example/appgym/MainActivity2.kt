package com.example.appgym

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
        val currentUser = auth.currentUser
        val menuBajo = findViewById<BottomNavigationView>(R.id.menuBajo)
        val userId = auth.currentUser!!.uid
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

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        val ventanaBloqueo = findViewById<LinearLayout>(R.id.ventanaInicial)
        db.collection("miembros").document(userId)
            .addSnapshotListener { snapshot, e ->
                if (kotlin.e != null || snapshot == null) return@addSnapshotListener

                val estadoMembresia = snapshot.getString("estado") // "activa", "inactiva", "vencida"

                if (estadoMembresia == "inactiva" || estadoMembresia == "vencida") {
                    // Mostrar pantalla de bloqueo y ocultar menú
                    ventanaBloqueo.visibility = View.VISIBLE
                    menuBajo.visibility = View.GONE
                } else {
                    // Ocultar pantalla de bloqueo y mostrar el contenido normal
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


    private fun escucharEstadoMembresia() {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            // Manejar caso donde el usuario no ha iniciado sesión
            Toast.makeText(this, "No hay sesión activa", Toast.LENGTH_SHORT).show()
            return
        }
}