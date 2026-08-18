package com.example.appgym

import android.content.Intent
import android.os.Bundle
import android.view.View
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
    private lateinit var menuBajo: BottomNavigationView
    private var primerCarga = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val currentUser = auth.currentUser
        if (currentUser == null) {
            // Si no hay usuario, redirigir a Login/Registro
            startActivity(Intent(this, MainActivity3::class.java))
            finish()
            return
        }

        menuBajo = findViewById(R.id.menuBajo)
        menuBajo.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.item_perfil -> Perfil()
                R.id.item_progreso -> Progreso()
                R.id.item_clases -> ClasesFragment()
                R.id.item_membresia -> Membresia()
                else -> Perfil()
            } as Fragment
            cargarFragment(fragment)
            true
        }

       // val passwordUsuario = "170225"

        db.collection("miembros")
            .whereEqualTo("uid", currentUser.uid)
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    android.util.Log.e("DEBUG_GYM", "Error de Firestore: ${e.message}")
                    return@addSnapshotListener
                }

                // Si el usuario no existe en la colección miembros -> Bloquear
                if (querySnapshot == null || querySnapshot.isEmpty) {
                    android.util.Log.d("DEBUG_GYM", "No se encontró registro para este usuario -> Bloqueando")
                    mostrarPantallaBloqueo()
                    return@addSnapshotListener
                }

                // Obtener el documento del usuario autenticado dinámicamente
                val document = querySnapshot.documents[0]
                val estadoMembresia = document.getString("estado")?.lowercase()?.trim().orEmpty()

                android.util.Log.d("DEBUG_GYM", "Usuario detectado: ${document.getString("nombre")} | Estado: '$estadoMembresia'")

                // Validación universal de estado
                if (estadoMembresia == "inactiva" || estadoMembresia == "vencida") {
                    mostrarPantallaBloqueo()
                } else {
                    menuBajo.visibility = View.VISIBLE
                    if (primerCarga && savedInstanceState == null) {
                        cargarFragment(Perfil())
                        primerCarga = false
                    }
                }
            }
    }

    private fun mostrarPantallaBloqueo() {
        // cultar menú inferior
        menuBajo.visibility = View.GONE

        // fragment de bloqueo
        cargarFragment(MembresiaBloqueadaFragment())
    }

    private fun cargarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedor_fragment, fragment)
            .commit()
    }
}




/*
import android.content.Intent
import android.os.Bundle
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
    private lateinit var menuBajo: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        // Inicializaciones de Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Validación de usuario autenticado
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, MainActivity3::class.java))
            finish()
            return
        }

        // Configuración de la navegación inferior
        menuBajo = findViewById(R.id.menuBajo)
        menuBajo.setOnItemSelectedListener { item ->: Fragment = when (item.itemId) {
                R.id.it
            val fragmentem_home -> home()
                R.id.item_perfil -> Perfil()
                R.id.item_progreso -> Progreso()
                R.id.item_clases -> Clases()
                R.id.item_membresia -> Membresia()
                else -> home()
            }
            cargarFragment(fragment)
            true
        }

        // Escuchar estado de la membresía en tiempo real
        db.collection("miembros").document(currentUser.uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null || !snapshot.exists()) return@addSnapshotListener

                val estadoMembresia = snapshot.getString("estado")?.lowercase()?.trim() ?:" "

                if (estadoMembresia == "inactiva" || estadoMembresia == "vencida") {
                    val intent = Intent(this, MainActivity3::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Cargar el fragmento inicial solo si no hay un fragmento previamente guardado
                    if (savedInstanceState == null) {
                        cargarFragment(home())
                    }
                }
            }
    }

    private fun cargarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedor_fragment, fragment)
            .commit()
    }
}*/