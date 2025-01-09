// MainActivity.kt
package com.example.appdam

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appdam.adapter.MainCategoryAdapter
import com.example.appdam.adapter.SubCategoryAdapter
import com.example.appdam.database.ReceitasDatabase
import com.example.appdam.entidades.Categoria
import com.example.appdam.entidades.CategoriaItens
import com.example.appdam.entidades.Receitas
import com.example.appdam.interfaces.GetDataService
import com.example.appdam.retrofitclient.RetrofitClient
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity(), EasyPermissions.RationaleCallbacks, EasyPermissions.PermissionCallbacks {
    private var READ_STORAGE_PERM = 123

    var arrMainCategory = ArrayList<CategoriaItens>()
    var arrSubCategory = ArrayList<Receitas>()
    var mainCategoryAdapter = MainCategoryAdapter()
    var subCategoryAdapter = SubCategoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.teste1)

        Log.d("DEBUG_FLOW", "onCreate chamado")
        initializeRecyclerViews()
        readStorageTask()
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
        Log.d("DEBUG_FLOW", "Verificando permissões...")
        if (hasReadStoragePermission()) {
            Log.d("DEBUG_FLOW", "Permissão concedida. Chamando getCategorias.")
            getCategorias()
        } else {
            Log.d("DEBUG_FLOW", "Permissão não concedida. Solicitando...")
            EasyPermissions.requestPermissions(
                this,
                "Esta aplicação precisa de acesso ao armazenamento.",
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
        Log.d("DEBUG_API", "getCategorias chamado")
        val service = RetrofitClient.retrofitInstance!!.create(GetDataService::class.java)
        val call = service.getCategoryList()
        call.enqueue(object : Callback<Categoria> {
            override fun onFailure(call: Call<Categoria>, t: Throwable) {
                Log.e("DEBUG_API", "Erro ao fazer chamada: ${t.message}")
                Toast.makeText(this@MainActivity, "Algo Correu Mal", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Categoria>, response: Response<Categoria>) {
                if (response.isSuccessful) {
                    Log.d("DEBUG_API", "Resposta recebida: ${response.body()}")
                    insertDataIntoRoomDb(response.body())
                } else {
                    Log.e("DEBUG_API", "Erro na resposta: ${response.code()} - ${response.message()}")
                }
            }
        })
    }

    private fun insertDataIntoRoomDb(categoria: Categoria?) {
        launch {
            this.let {
                ReceitasDatabase.getDatabase(this@MainActivity).receitasDao().clearDb()
                for (arr in categoria?.categoriasItens ?: emptyList()) {
                    ReceitasDatabase.getDatabase(this@MainActivity).receitasDao().insertCategoria(arr)
                    Log.d("DEBUG_DB", "Categoria inserida: $arr")
                }
                getDataFromDb()
            }
        }
    }

    private fun getDataFromDb() {
        launch {
            this.let {
                val categorias = ReceitasDatabase.getDatabase(this@MainActivity).receitasDao().getAllCategory()
                Log.d("DEBUG_DB", "Categorias recuperadas: $categorias")
                arrMainCategory = categorias as ArrayList<CategoriaItens>
                mainCategoryAdapter.setData(arrMainCategory)
            }
        }
    }
}



