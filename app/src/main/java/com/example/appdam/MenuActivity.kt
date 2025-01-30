package com.example.appdam

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.appdam.databinding.ActivityMainBinding
import com.example.appdam.utils.SharedPreferencesHelper
import com.google.android.material.navigation.NavigationView

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferencesHelper = SharedPreferencesHelper(this)

        // Obter o nome do usuário do SharedPreferences
        val userName = sharedPreferencesHelper.getUserName() ?: "Usuário desconhecido"

        // Configurar o DrawerLayout e NavigationView
        drawerLayout = binding.drawerLayout
        val navView: NavigationView = findViewById(R.id.nav_view)
        val openMenuButton: ImageButton = findViewById(R.id.open_menu_button)

        // Botão para abrir o menu lateral
        openMenuButton.setOnClickListener {
            drawerLayout.openDrawer(navView)
        }

        // Configurar o cabeçalho do NavigationView para mostrar o nome do usuário
        val headerView = navView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.user_name)
        userNameTextView.text = "Bem vindo, $userName"

        // Configurar os itens do menu lateral
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
                    sharedPreferencesHelper.clearToken()
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
