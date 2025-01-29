package com.example.appdam

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.appdam.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = findViewById(R.id.nav_view)
        val openMenuButton: ImageButton = findViewById(R.id.open_menu_button)

        // Configura o clique no botão para abrir o menu lateral
        openMenuButton.setOnClickListener {
            drawerLayout.openDrawer(navView)
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_receitas -> {
                    startActivity(Intent(this, ReceitasActivity::class.java))
                }
                R.id.nav_comentarios -> {
                    startActivity(Intent(this, ReceitasActivity::class.java))
                }
                R.id.nav_info -> {
                    startActivity(Intent(this, ReceitasActivity::class.java))
                }
                R.id.nav_logout -> {
                    val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    sharedPref.edit().clear().apply()
                    Toast.makeText(this, "Logout realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                else -> {
                    Toast.makeText(this, "Opção inválida.", Toast.LENGTH_SHORT).show()
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }
}
