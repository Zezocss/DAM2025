package com.example.appdam

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var READ_STORAGE_PERM = 123

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa o SharedPreferencesHelper
        sharedPreferencesHelper = SharedPreferencesHelper(this)

        // Verifica se o usuário está logado
        val token = sharedPreferencesHelper.getToken()
        if (!token.isNullOrEmpty()) {
            // Se o token existe, exibe uma mensagem de boas-vindas
            Toast.makeText(this, "Bem-vindo de volta!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        } else {
            // Se não houver token, redireciona para a LoginActivity
            Toast.makeText(this, "Token não encontrado. Faça login novamente.", Toast.LENGTH_SHORT)
                .show()
            redirectToLogin()
        }

        }
    private fun redirectToLogin() {
        sharedPreferencesHelper.clearToken()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
