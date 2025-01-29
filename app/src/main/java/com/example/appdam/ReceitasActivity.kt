package com.example.appdam

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appdam.adapter.MainCategoryAdapter
import com.example.appdam.adapter.SubCategoryAdapter
import com.example.appdam.database.ReceitasDatabase
import com.example.appdam.entidades.Categoria
import com.example.appdam.entidades.CategoriaItens
import com.example.appdam.entidades.Prato
import com.example.appdam.entidades.PratosItens
import com.example.appdam.interfaces.GetDataService
import com.example.appdam.retrofitclient.RetrofitClient
import com.example.appdam.utils.SharedPreferencesHelper
import com.example.appdam.MenuActivity
import com.example.appdam.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReceitasActivity : BaseActivity(), EasyPermissions.RationaleCallbacks, EasyPermissions.PermissionCallbacks {
    private var READ_STORAGE_PERM = 123

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    var arrMainCategory = ArrayList<CategoriaItens>()
    var arrSubCategory = ArrayList<PratosItens>()
    var mainCategoryAdapter = MainCategoryAdapter()
    var subCategoryAdapter = SubCategoryAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configura o layout com o DrawerLayout
        setupDrawer(R.layout.teste1)

        // Configura o botão para abrir o menu lateral
        val openMenuButton: ImageButton = findViewById(R.id.open_menu_button)
        openMenuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Configura o NavigationView para gerenciar as opções do menu lateral
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_receitas -> {
                    Toast.makeText(this, "Já está na página de receitas!", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_comentarios -> {
                    startActivity(Intent(this, ReceitasActivity::class.java))
                }
                R.id.nav_info -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                }
                R.id.nav_logout -> {
                    val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    sharedPref.edit().clear().apply()
                    Toast.makeText(this, "Logout realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
            drawerLayout.closeDrawers()
            true
        }

        // Debug para verificar o fluxo de execução
        Log.d("DEBUG_FLOW", "onCreate chamado")

        // Inicializa os RecyclerViews e carrega os dados
        initializeRecyclerViews()
        readStorageTask()
        getDataFromDb()

        // Configura os listeners para cliques nos adapters
        mainCategoryAdapter.setClicklistener(onClicked)
        subCategoryAdapter.setClicklistener(onClickedSubItens)
    }

    private val onClicked = object : MainCategoryAdapter.onItemClickListener {
        override fun onClicked(categoryName: String) {
            getPratoDataFromDb(categoryName)
        }
    }

    private val onClickedSubItens = object : SubCategoryAdapter.onItemClickListener {
        override fun onClicked(id: String) {
            val intent = Intent(this@ReceitasActivity, DetailActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }


    private fun initializeRecyclerViews() {
        val rvMainCategory = findViewById<RecyclerView>(R.id.rv_main_category)
        rvMainCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvMainCategory.adapter = mainCategoryAdapter

        val rvSubCategory = findViewById<RecyclerView>(R.id.rv_sub_category)
        rvSubCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvSubCategory.adapter = subCategoryAdapter
    }

    private fun readStorageTask() {
        if (Build.VERSION.SDK_INT >= 33) { // Android 13 ou superior
            EasyPermissions.requestPermissions(
                this,
                "Esta aplicação precisa de acesso ao armazenamento para carregar as categorias.",
                READ_STORAGE_PERM,
                android.Manifest.permission.READ_MEDIA_IMAGES,
                android.Manifest.permission.READ_MEDIA_VIDEO
            )
        } else { // Android 12 ou inferior
            EasyPermissions.requestPermissions(
                this,
                "Esta aplicação precisa de acesso ao armazenamento para carregar as categorias.",
                READ_STORAGE_PERM,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun hasReadStoragePermission(): Boolean {
        return EasyPermissions.hasPermissions(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onRationaleAccepted(requestCode: Int) {
        Log.d("DEBUG_FLOW", "Permissão aceita pelo usuário.")
    }

    override fun onRationaleDenied(requestCode: Int) {
        Log.d("DEBUG_FLOW", "Permissão negada pelo usuário.")
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.d("DEBUG_FLOW", "Permissão concedida pelo usuário. Chamando getCategorias.")
        getCategorias()
        clearDatabase()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Log.d("DEBUG_FLOW", "Permissão permanentemente negada. Mostrar diálogo.")
            AppSettingsDialog.Builder(this).build().show()
        } else {
            Log.d("DEBUG_FLOW", "Permissão negada pelo usuário.")
        }
    }

    private fun getCategorias() {
        val service = RetrofitClient.retrofitInstance.create(GetDataService::class.java)
        val call = service.getCategoryList()

        call.enqueue(object : Callback<Categoria> {
            override fun onFailure(call: Call<Categoria>, t: Throwable) {
                Toast.makeText(
                    this@ReceitasActivity, "Erro ao conectar ao servidor. Verifica a tua conexão.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(call: Call<Categoria>, response: Response<Categoria>) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.categories?.forEach { arr ->
                        getPratos(arr.strCategory) // Adiciona lógica para tratar pratos principais
                    }
                    insertDataIntoRoomDb(response.body())
                } else {
                    Toast.makeText(
                        this@ReceitasActivity, "Erro na resposta da API. Código ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun getPratos(categoriaName: String) {
        val service = RetrofitClient.retrofitInstance.create(GetDataService::class.java)
        val call = service.getMealList(categoriaName)

        call.enqueue(object : Callback<Prato> {
            override fun onFailure(call: Call<Prato>, t: Throwable) {
                Toast.makeText(
                    this@ReceitasActivity, "Erro ao conectar ao servidor. Verifica a tua conexão.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(call: Call<Prato>, response: Response<Prato>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d("API_RESPONSE", "Pratos recebidos: ${response.body()?.pratosItens}")
                    insertPratoDataIntoRoomDb(categoriaName, response.body()) // Passa os dados para o banco de dados
                } else {
                    Log.e("API_RESPONSE", "Erro na API: ${response.code()} - ${response.message()}")
                    Toast.makeText(
                        this@ReceitasActivity, "Erro na resposta da API. Código ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun insertDataIntoRoomDb(categoria: Categoria?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Insere as categorias no banco de dados
                categoria?.categories?.forEach { arr ->
                    ReceitasDatabase.getDatabase(this@ReceitasActivity).receitasDao().insertCategoria(arr)
                    Log.d("DEBUG_DB", "Categoria inserida: $arr")
                }

                // Atualiza os dados da UI numa Thread principal
                withContext(Dispatchers.Main) {
                    getDataFromDb()
                }
            } catch (e: Exception) {
                Log.e("DEBUG_DB", "Erro ao inserir dados na base de dados: ${e.message}")
            }
        }
    }

    private fun insertPratoDataIntoRoomDb(categoriaName: String, prato: Prato?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Insere as categorias no banco de dados
                prato?.pratosItens?.forEach { arr ->
                    Log.d("DATABASE_INSERT", "Prato a ser salvo: $arr")
                    val pratoItemModel = PratosItens(
                        arr.id,
                        arr.idMeal,
                        categoriaName,
                        arr.strMeal,
                        arr.strMealThumb
                    )
                    ReceitasDatabase.getDatabase(this@ReceitasActivity).receitasDao().insertPrato(pratoItemModel)
                    Log.d("Dados dos Pratos", arr.toString())
                }

                // Atualiza os dados da UI numa Thread principal
                withContext(Dispatchers.Main) {
                    getDataFromDb()
                }
            } catch (e: Exception) {
                Log.e("DEBUG_DB", "Erro ao inserir dados na base de dados: ${e.message}")
            }
        }
    }

    private fun clearDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Limpa o banco de dados
                ReceitasDatabase.getDatabase(this@ReceitasActivity).receitasDao().clearDb()
                Log.d("DEBUG_DB", "Base de dados limpa.")
            } catch (e: Exception) {
                Log.e("DEBUG_DB", "Erro ao carregar dados do banco: ${e.message}")
            }
        }
    }

    private fun getDataFromDb() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val categorias = ReceitasDatabase.getDatabase(this@ReceitasActivity).receitasDao().getAllCategory()
                arrMainCategory = categorias as ArrayList<CategoriaItens>
                arrMainCategory.reverse() // Reverte para manter a ordem se necessário
                getPratoDataFromDb(arrMainCategory[0].strCategory)

                withContext(Dispatchers.Main) {
                    // Atualiza o RecyclerView com os dados carregados
                    mainCategoryAdapter.setData(categorias)
                    mainCategoryAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("DEBUG_DB", "Erro ao carregar dados do banco: ${e.message}")
            }
        }
    }

    private fun getPratoDataFromDb(categoryName: String) {
        val tvCategory = findViewById<TextView>(R.id.tvCategory)
        tvCategory.text = "$categoryName categoria"
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pratos = ReceitasDatabase.getDatabase(this@ReceitasActivity).receitasDao().getSpecificPrato(categoryName)
                Log.d("DATABASE_QUERY", "Pratos carregados: $pratos")

                arrSubCategory = pratos as ArrayList<PratosItens>

                withContext(Dispatchers.Main) {
                    // Atualiza o RecyclerView com os dados carregados
                    Log.d("ADAPTER_DATA", "Dados a serem exibidos: $arrSubCategory")
                    subCategoryAdapter.setData(arrSubCategory)
                    subCategoryAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("DEBUG_DB", "Erro ao carregar dados do banco: ${e.message}")
            }
        }
    }
}
