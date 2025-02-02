package com.example.appdam.receitasuser

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.appdam.R
import com.example.appdam.api.RetrofitUser
import com.example.appdam.entidades.Receita
import com.example.appdam.receitasuser.add.ReceitaRequest
import com.example.appdam.utils.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditReceitaActivity : AppCompatActivity() {

    private lateinit var etTituloEdit: EditText
    private lateinit var ivFotoReceitaEdit: ImageView
    private lateinit var etIngredientesEdit: EditText
    private lateinit var etPreparoEdit: EditText
    private lateinit var btnSalvarEdit: Button
    private lateinit var btnEliminar: Button

    private var receitaAtual: Receita? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_receita)

        etTituloEdit = findViewById(R.id.etTituloEdit)
        ivFotoReceitaEdit = findViewById(R.id.ivFotoReceitaEdit)
        etIngredientesEdit = findViewById(R.id.etIngredientesEdit)
        etPreparoEdit = findViewById(R.id.etPreparoEdit)
        btnSalvarEdit = findViewById(R.id.btnSalvarEdit)
        btnEliminar = findViewById(R.id.btnEliminar)

        val btnBack: ImageButton = findViewById(R.id.imgToolbarBtnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, ListaReceitaActivity::class.java)
            startActivity(intent)
            finish()
        }

        val idReceita = intent.getLongExtra("ID_RECEITA", -1)
        if (idReceita != -1L) {
            carregarReceita(idReceita)
        }

        btnSalvarEdit.setOnClickListener {
            atualizarReceita()
        }

        btnEliminar.setOnClickListener {
            excluirReceita()
        }
    }

    private fun carregarReceita(idReceita: Long) {
        val username = SharedPreferencesHelper(this).getUserName()

        if (username.isNullOrEmpty()) {
            Toast.makeText(this, "Erro: Nome do utilizador não encontrado!", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitUser.instance.getReceitas(username).enqueue(object : Callback<List<Receita>> {
            override fun onResponse(call: Call<List<Receita>>, response: Response<List<Receita>>) {
                if (response.isSuccessful) {
                    receitaAtual = response.body()?.find { it.id == idReceita }
                    receitaAtual?.let { it ->
                        etTituloEdit.setText(it.titulo)
                        etIngredientesEdit.setText(it.ingredientes)
                        etPreparoEdit.setText(it.preparo)
                        Glide.with(this@EditReceitaActivity)
                            .load(it.fotourl)
                            .into(ivFotoReceitaEdit)
                    }
                } else {
                    Toast.makeText(this@EditReceitaActivity, "Erro ao carregar receita.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Receita>>, t: Throwable) {
                Toast.makeText(this@EditReceitaActivity, "Erro de conexão: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun atualizarReceita() {
        val receitaAtualizada = receitaAtual?.let {
            ReceitaRequest(
                titulo = etTituloEdit.text.toString(),
                fotourl = it.fotourl, // Preservar o URL da foto original
                ingredientes = etIngredientesEdit.text.toString(),
                preparo = etPreparoEdit.text.toString(),
                username = it.username // Usar o username já associado à receita
            )
        }

        if (receitaAtualizada == null) {
            Toast.makeText(this, "Erro: receita inválida!", Toast.LENGTH_SHORT).show()
            return
        }

        receitaAtual?.id?.let { id ->
            RetrofitUser.instance.updateReceita(id, receitaAtualizada)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@EditReceitaActivity, "Receita atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@EditReceitaActivity, "Erro ao atualizar receita.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@EditReceitaActivity, "Erro de conexão: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        } ?: run {
            Toast.makeText(this, "Erro: ID da receita inválido!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun excluirReceita() {
        val username = SharedPreferencesHelper(this).getUserName()

        if (username.isNullOrEmpty()) {
            Toast.makeText(this, "Erro: Nome do utilizador não encontrado!", Toast.LENGTH_SHORT).show()
            return
        }

        receitaAtual?.let {
            RetrofitUser.instance.deleteReceita(it.id!!, username)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@EditReceitaActivity, "Receita eliminada!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@EditReceitaActivity, "Erro ao eliminar receita.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@EditReceitaActivity, "Erro de conexão: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

}
