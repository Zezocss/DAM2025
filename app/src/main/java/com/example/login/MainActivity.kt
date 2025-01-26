package com.example.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.login.utils.SharedPreferencesHelper

class MainActivity : AppCompatActivity() {

    // Declaração de SharedPreferencesHelper
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa o SharedPreferencesHelper
        sharedPreferencesHelper = SharedPreferencesHelper(this)

        // Recupera o token armazenado
        val token = sharedPreferencesHelper.getToken()

        if (!token.isNullOrEmpty()) {
            // Se o token existe, exibe uma mensagem de boas-vindas
            Toast.makeText(this, "Bem-vindo de volta!", Toast.LENGTH_SHORT).show()
        } else {
            // Se não houver token, redireciona para a LoginActivity
            Toast.makeText(this, "Token não encontrado. Faça login novamente.", Toast.LENGTH_SHORT).show()
            redirectToLogin()
        }
    }

    private fun redirectToLogin() {
        // Limpa o token e redireciona para a LoginActivity
        sharedPreferencesHelper.clearToken()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
