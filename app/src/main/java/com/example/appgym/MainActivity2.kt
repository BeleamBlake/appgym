package com.example.appgym

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity2 : AppCompatActivity() {

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

        val menuBajo = findViewById<BottomNavigationView>(R.id.menuBajo)

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
    }

    private fun cargarFragment(fragment: Fragment) {

        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedor_fragment, fragment)
            .commit()
    }
}