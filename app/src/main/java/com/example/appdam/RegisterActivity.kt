package com.example.appdam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appdam.auth.RetrofitAuth
import com.example.appdam.models.RegisterRequest
import com.example.appdam.utils.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sharedPreferencesHelper = SharedPreferencesHelper(this)

        val buttonBack: ImageButton = findViewById(R.id.buttonBack)
        buttonBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        val registerButton = findViewById<Button>(R.id.buttonRegister)
        registerButton.setOnClickListener {
            val firstName = findViewById<EditText>(R.id.editTextFirstName).text.toString()
            val lastName = findViewById<EditText>(R.id.editTextLastName).text.toString()
            val email = findViewById<EditText>(R.id.editTextEmail).text.toString()
            val username = findViewById<EditText>(R.id.editTextUsername).text.toString()
            val password = findViewById<EditText>(R.id.editTextPassword).text.toString()
            registerUser(firstName, lastName, email, username, password)
        }
    }

    // Regista um novo utilizador na API
    private fun registerUser(firstName: String, lastName: String, email: String, username: String, password: String) {
        val registerRequest = RegisterRequest(firstName, lastName, email, username, password)

        RetrofitAuth.instance.registerUser(registerRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Salvar o nome completo
                    sharedPreferencesHelper.saveUserName("$firstName $lastName")
                    Toast.makeText(this@RegisterActivity, "Registo bem-sucedido!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@RegisterActivity, "Altere seu UserName!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Erro: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
