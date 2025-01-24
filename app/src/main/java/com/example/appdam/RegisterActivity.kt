package com.example.appdam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appdam.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var genderSwitch: Switch
    private lateinit var genderTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializa os componentes
        firstNameEditText = findViewById(R.id.firstNameText)
        lastNameEditText = findViewById(R.id.lastNameText)
        emailEditText = findViewById(R.id.emailText)
        usernameEditText = findViewById(R.id.usernameText)
        passwordEditText = findViewById(R.id.passwordText)
        genderSwitch = findViewById(R.id.genderSwitch)
        genderTextView = findViewById(R.id.genderTextView)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val loginLink = findViewById<TextView>(R.id.loginLink)

        // Define o texto inicial para o gênero
        genderTextView.text = if (genderSwitch.isChecked) "Masculino" else "Feminino"

        // Atualiza o texto ao alternar o Switch
        genderSwitch.setOnCheckedChangeListener { _, isChecked ->
            genderTextView.text = if (isChecked) "Masculino" else "Feminino"
        }

        // Configura o botão de registro
        registerButton.setOnClickListener {
            performRegistration()
        }

        // Navega para a tela de login
        loginLink.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun performRegistration() {
        val firstName = firstNameEditText.text.toString().trim()
        val lastName = lastNameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val gender = if (genderSwitch.isChecked) "Masculino" else "Feminino"

        // Validação dos campos
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Email inválido"
            emailEditText.requestFocus()
            return
        }

        if (password.length < 8) {
            passwordEditText.error = "A senha deve ter pelo menos 8 caracteres"
            passwordEditText.requestFocus()
            return
        }

        // Registro bem-sucedido
        Toast.makeText(this, "Registro realizado com sucesso!", Toast.LENGTH_SHORT).show()

        // Navega para MainActivity com a flag "fromRegister"
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("fromRegister", true) // Sinaliza que veio do registro
        startActivity(intent)
        finish()
    }
}