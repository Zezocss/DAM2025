package com.example.appdam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appdam.models.LoginRequest
import com.example.appdam.models.LoginResponse
import com.example.appdam.utils.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.appdam.auth.RetrofitAuth
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferencesHelper = SharedPreferencesHelper(this)

        // Verifica se a política já foi aceite, caso contrário mostra o diálogo
        if (!sharedPreferencesHelper.isPrivacyPolicyAccepted()) {
            mostrarDialogoDeConsentimento()
        }

        // Botão de Login
        val loginButton = findViewById<Button>(R.id.buttonLogin)
        loginButton.setOnClickListener {
            val username = findViewById<EditText>(R.id.editTextUsername).text.toString()
            val password = findViewById<EditText>(R.id.editTextPassword).text.toString()
            loginUser(username, password)
        }

        // Adiciona funcionalidade de clique longo ao botão de login para redefinir a política
        loginButton.setOnLongClickListener {
            redefinirPoliticaPrivacidade()
            true
        }

        // Botão de Registro
        val registerButton = findViewById<Button>(R.id.buttonRegister)
        registerButton.setOnClickListener {
            goToRegister()
        }
    }

    private fun loginUser(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)

        RetrofitAuth.instance.loginUser(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (token != null) {
                        sharedPreferencesHelper.saveToken(token)
                        Toast.makeText(this@LoginActivity, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Erro ao obter token!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Credenciais inválidas!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Erro: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun goToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun mostrarDialogoDeConsentimento() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Aviso de Privacidade")
            .setMessage(
                "A sua privacidade é importante para nós. Os seus dados pessoais são usados apenas para garantir o correto funcionamento da aplicação." +
                        "As informações nunca serão partilhadas com terceiros. Implementamos medidas de segurança para proteger os dados de acessos não autorizados. " +
                        "Ao utilizar a aplicação, está a concordar com esta política. Pode redefinir a aceitação da política nas definições da app."
            )
            .setCancelable(false) // Não permite fechar sem interagir
            .setPositiveButton("Aceitar") { _, _ ->
                sharedPreferencesHelper.savePrivacyPolicyAccepted()
                Toast.makeText(this, "Política de Privacidade aceite!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Recusar") { _, _ ->
                terminarAplicacao()
            }
            .show()
    }

    private fun redefinirPoliticaPrivacidade() {
        sharedPreferencesHelper.resetPrivacyPolicy()
        Toast.makeText(this, "Política de Privacidade foi redefinida!", Toast.LENGTH_SHORT).show()
        mostrarDialogoDeConsentimento()
    }

    private fun terminarAplicacao() {
        finish() // Fecha a aplicação completamente
    }
}

