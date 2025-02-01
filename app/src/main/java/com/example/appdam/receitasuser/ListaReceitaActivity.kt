package com.example.appdam.receitasuser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appdam.R
import com.example.appdam.adapter.ReceitaAdapter
import com.example.appdam.entidades.Receita
import com.example.appdam.receitasuser.RetrofitUser
import com.example.appdam.receitasuser.add.ResponseAddReceitas
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaReceitaActivity : AppCompatActivity() {

    private lateinit var rvReceitas: RecyclerView
    private lateinit var receitaAdapter: ReceitaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_receita)

        rvReceitas = findViewById(R.id.rvReceitas)
        val fabAddReceita = findViewById<FloatingActionButton>(R.id.fabAddReceita)

        receitaAdapter = ReceitaAdapter(emptyList()) { receita ->
            val intent = Intent(this, EditReceitaActivity::class.java)
            intent.putExtra("ID_RECEITA", receita.id)
            startActivity(intent)
        }
        rvReceitas.layoutManager = LinearLayoutManager(this)
        rvReceitas.adapter = receitaAdapter

        fabAddReceita.setOnClickListener {
            startActivity(Intent(this, AddReceitaActivity::class.java))
        }

        carregarReceitasDoSheety()
    }

    // 🔥 NOVO: Atualiza a lista automaticamente sempre que o usuário voltar para esta tela
    override fun onResume() {
        super.onResume()
        carregarReceitasDoSheety() // Recarrega as receitas ao voltar para a tela
    }

    private fun carregarReceitasDoSheety() {
        RetrofitUser.instance.getReceitas().enqueue(object : Callback<ResponseReceitas> {
            override fun onResponse(call: Call<ResponseReceitas>, response: Response<ResponseReceitas>) {
                if (response.isSuccessful) {
                    val receitas = response.body()?.folha1 ?: emptyList()

                    // Log para depuração - Mostra os IDs carregados
                    for (receita in receitas) {
                        Log.d("DEBUG", "Receita ID: ${receita.id}, Título: ${receita.titulo}")
                    }

                    receitaAdapter.atualizarLista(receitas)
                } else {
                    Toast.makeText(this@ListaReceitaActivity, "Erro ao carregar receitas!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseReceitas>, t: Throwable) {
                Log.e("API_ERROR", "Erro ao buscar receitas: ${t.message}")
            }
        })
    }
}
