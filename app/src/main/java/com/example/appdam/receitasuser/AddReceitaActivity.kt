// AddReceitaActivity.kt
package com.example.appdam.receitasuser

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appdam.R
import com.example.appdam.api.RetrofitUser
import com.example.appdam.receitasuser.add.ReceitaRequest
import com.example.appdam.utils.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddReceitaActivity : AppCompatActivity() {

    private lateinit var etTitulo: EditText
    private lateinit var etIngredientes: EditText
    private lateinit var etPreparo: EditText
    private lateinit var btnSalvarReceita: Button
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_receita)

        sharedPreferencesHelper = SharedPreferencesHelper(this)

        etTitulo = findViewById(R.id.etTitulo)
        etIngredientes = findViewById(R.id.etIngredientes)
        etPreparo = findViewById(R.id.etPreparo)
        btnSalvarReceita = findViewById(R.id.btnSalvarReceita)

        val btnBack: ImageButton = findViewById(R.id.imgToolbarBtnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, ListaReceitaActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSalvarReceita.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val ingredientes = etIngredientes.text.toString()
            val preparo = etPreparo.text.toString()
            val username = sharedPreferencesHelper.getUserName()
            if (username.isNullOrEmpty()) {
                Toast.makeText(this, "Erro: nome do utilizador não encontrado!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val receitaRequest = ReceitaRequest(
                titulo = titulo,
                fotourl = "",
                ingredientes = ingredientes,
                preparo = preparo,
                username = username
            )

            salvarReceita(receitaRequest)
        }
    }

    private fun salvarReceita(receitaRequest: ReceitaRequest) {
        RetrofitUser.instance.addReceita(receitaRequest)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddReceitaActivity, "Receita adicionada com sucesso!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddReceitaActivity, "Erro ao guardar receita.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@AddReceitaActivity, "Erro de conexão: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
