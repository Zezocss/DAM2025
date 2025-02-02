// ListaReceitaActivity.kt
package com.example.appdam.receitasuser

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appdam.BaseActivity
import com.example.appdam.R
import com.example.appdam.adapter.ReceitaAdapter
import com.example.appdam.api.RetrofitUser
import com.example.appdam.entidades.Receita
import com.example.appdam.utils.SharedPreferencesHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaReceitaActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var receitaAdapter: ReceitaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Usa o método da BaseActivity para configurar o menu lateral
        setupDrawer(R.layout.activity_lista_receita)

        // Configurar o botão de abrir menu lateral
        val openMenuButton: ImageButton = findViewById(R.id.open_menu_button)
        openMenuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.rvReceitas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Criar o adaptador (inicialmente vazio)
        receitaAdapter = ReceitaAdapter(emptyList()) { receita ->
            val intent = Intent(this, EditReceitaActivity::class.java)
            intent.putExtra("ID_RECEITA", receita.id)
            startActivity(intent)
        }

        recyclerView.adapter = receitaAdapter

        // Carregar receitas
        carregarReceitas()

        // Configurar botão flutuante para adicionar receita
        val fabAddReceita: FloatingActionButton = findViewById(R.id.fabAddReceita)
        fabAddReceita.setOnClickListener {
            val intent = Intent(this, AddReceitaActivity::class.java)
            startActivity(intent)
        }


        recyclerView.adapter = receitaAdapter

        carregarReceitas()

    }

    private fun carregarReceitas() {
        // Obter o nome do utilizador das preferências partilhadas
        val username = SharedPreferencesHelper(this).getUserName()

        // Verificar se o nome do utilizador é válido
        if (username.isNullOrEmpty()) {
            Toast.makeText(this, "Erro: Nome do utilizador não encontrado nas preferências!", Toast.LENGTH_SHORT).show()
            return
        }

        // Fazer a requisição para obter as receitas associadas ao utilizador
        RetrofitUser.instance.getReceitas(username).enqueue(object : Callback<List<Receita>> {
            override fun onResponse(call: Call<List<Receita>>, response: Response<List<Receita>>) {
                if (response.isSuccessful) {
                    val receitas = response.body()
                    if (!receitas.isNullOrEmpty()) {
                        receitaAdapter.atualizarLista(receitas)
                    } else {
                        Toast.makeText(this@ListaReceitaActivity, "Ainda não criou nenhuma receita!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ListaReceitaActivity, "Erro ao carregar receitas.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Receita>>, t: Throwable) {
                Toast.makeText(this@ListaReceitaActivity, "Erro de conexão: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onResume() {
        super.onResume()
        carregarReceitas()
    }
}
