package com.example.appgym

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity2 : AppCompatActivity() {

   private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val bottomNav = findViewById<BottomNavigationView>(R.id.menuBajo)

        // aqui se declara la ventana inicial
        // aqui es para el cambio de ventanas
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.ventanaInicial, home())
                        .commit()

                    true
                }

                R.id.item_perfil -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.ventanaInicial, Perfil())
                        .commit()

                    true
                }

                R.id.item_progreso -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.ventanaInicial, Progreso())
                        .commit()

                    true
                }

                R.id.item_membresia -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.ventanaInicial, Membresia())
                        .commit()
                    true
                }

                R.id.item_clases -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.ventanaInicial, Clases())
                        .commit()

                    true
                }

                else -> false
            }
        }
    }
    }

