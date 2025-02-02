package com.example.appdam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appdam.adapter.MainCategoryAdapter
import com.example.appdam.adapter.SubCategoryAdapter
import com.example.appdam.entidades.Categoria
import com.example.appdam.entidades.CategoriaItens
import com.example.appdam.entidades.Prato
import com.example.appdam.entidades.PratosItens
import com.example.appdam.interfaces.GetDataService
import com.example.appdam.receitasuser.ListaReceitaActivity
import com.example.appdam.retrofitclient.RetrofitClient
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReceitasActivity : BaseActivity() {

    private val arrMainCategory = ArrayList<CategoriaItens>() // Lista para categorias
    private val arrSubCategory = ArrayList<PratosItens>()     // Lista para pratos
    private val mainCategoryAdapter = MainCategoryAdapter()
    private val subCategoryAdapter = SubCategoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configura o layout
        setupDrawer(R.layout.teste1)

        // Configura o botão para abrir o menu lateral
        val openMenuButton: ImageButton = findViewById(R.id.open_menu_button)
        openMenuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Configura o menu lateral
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_receitas -> {
                    Toast.makeText(this, "Já está na página de receitas!", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_suasreceitas -> {
                    startActivity(Intent(this, ListaReceitaActivity::class.java))
                }
                R.id.nav_info -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                }
                R.id.nav_logout -> {
                    Toast.makeText(this, "Logout Realizado com Sucesso", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
            drawerLayout.closeDrawers()
            true
        }

        // Inicializa os RecyclerViews e carrega os dados
        initializeRecyclerViews()
        getCategorias()

        // Configura os listeners para os cliques nos adapters
        mainCategoryAdapter.setClicklistener(onClicked)
        subCategoryAdapter.setClicklistener(onClickedSubItens)
    }

    // Listener para cliques nas categorias
    private val onClicked = object : MainCategoryAdapter.onItemClickListener {
        override fun onClicked(categoryName: String) {
            getPratos(categoryName)
        }
    }

    // Listener para cliques nos pratos
    private val onClickedSubItens = object : SubCategoryAdapter.onItemClickListener {
        override fun onClicked(id: String) {
            val intent = Intent(this@ReceitasActivity, DetailActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }

    // Inicializa os RecyclerViews
    private fun initializeRecyclerViews() {
        val rvMainCategory = findViewById<RecyclerView>(R.id.rv_main_category)
        rvMainCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvMainCategory.adapter = mainCategoryAdapter

        val rvSubCategory = findViewById<RecyclerView>(R.id.rv_sub_category)
        rvSubCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvSubCategory.adapter = subCategoryAdapter
    }

    // Faz a requisição para buscar categorias
    private fun getCategorias() {
        val service = RetrofitClient.retrofitInstance.create(GetDataService::class.java)
        val call = service.getCategoryList()

        call.enqueue(object : Callback<Categoria> {
            override fun onFailure(call: Call<Categoria>, t: Throwable) {
                Toast.makeText(this@ReceitasActivity, "Erro ao buscar categorias. Verifique sua conexão.", Toast.LENGTH_SHORT).show()
                Log.e("API_ERROR", "Erro ao buscar categorias: ${t.message}")
            }

            override fun onResponse(call: Call<Categoria>, response: Response<Categoria>) {
                if (response.isSuccessful && response.body() != null) {
                    arrMainCategory.clear()
                    arrMainCategory.addAll(response.body()?.categories ?: emptyList())
                    mainCategoryAdapter.setData(arrMainCategory)
                    mainCategoryAdapter.notifyDataSetChanged()

                    // Adiciona lógica para carregar as receitas da primeira categoria automaticamente
                    if (arrMainCategory.isNotEmpty()) {
                        val firstCategory = arrMainCategory[0].strCategory
                        getPratos(firstCategory) // Busca os pratos da primeira categoria
                    }
                } else {
                    Toast.makeText(this@ReceitasActivity, "Erro na resposta da API de categorias.", Toast.LENGTH_SHORT).show()
                    Log.e("API_ERROR", "Erro na resposta da API: ${response.code()} - ${response.message()}")
                }
            }
        })
    }

    // Faz a requisição para buscar pratos de uma categoria
    private fun getPratos(categoriaName: String) {
        val service = RetrofitClient.retrofitInstance.create(GetDataService::class.java)
        val call = service.getMealList(categoriaName)

        call.enqueue(object : Callback<Prato> {
            override fun onFailure(call: Call<Prato>, t: Throwable) {
                Toast.makeText(this@ReceitasActivity, "Erro ao buscar pratos. Verifique sua conexão.", Toast.LENGTH_SHORT).show()
                Log.e("API_ERROR", "Erro ao buscar pratos: ${t.message}")
            }

            override fun onResponse(call: Call<Prato>, response: Response<Prato>) {
                if (response.isSuccessful && response.body() != null) {
                    arrSubCategory.clear()
                    arrSubCategory.addAll(response.body()?.pratosItens ?: emptyList())
                    subCategoryAdapter.setData(arrSubCategory)
                    subCategoryAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@ReceitasActivity, "Erro na resposta da API de pratos.", Toast.LENGTH_SHORT).show()
                    Log.e("API_ERROR", "Erro na resposta da API: ${response.code()} - ${response.message()}")
                }
            }
        })
    }
}
