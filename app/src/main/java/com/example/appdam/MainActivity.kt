package com.example.appdam

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appdam.utils.SharedPreferencesHelper

class MainActivity : AppCompatActivity() {
    private var READ_STORAGE_PERM = 123

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.teste1)

        // Inicializa o SharedPreferencesHelper
        sharedPreferencesHelper = SharedPreferencesHelper(this)

        // Verifica se o utilizador está logado
        val token = sharedPreferencesHelper.getToken()
        if (!token.isNullOrEmpty()) {
            // Se o token existe, exibe uma mensagem de boas-vindas
            Toast.makeText(this, "Bem-vindo de volta!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, ReceitasActivity::class.java))
            finish()
        } else {
            // Se não houver token, redireciona para a LoginActivity

            redirectToLogin()
        }

        }
    private fun redirectToLogin() {
        sharedPreferencesHelper.clearToken()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
