package com.example.appdam


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appdam.adapter.MainCategoryAdapter
import com.example.appdam.adapter.SubCategoryAdapter
import com.example.appdam.entidades.Categoria
import com.example.appdam.entidades.Receitas
import com.example.appdam.interfaces.GetDataService
import com.example.appdam.retrofitclient.RetrofitClient
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.Locale
import java.util.Locale.Category
import java.util.jar.Manifest


class MainActivity : BaseActivity(), EasyPermissions.RationaleCallbacks, EasyPermissions.PermissionCallbacks {
    private var READ_STORAGE_PERM = 123

    var arrMainCategory = ArrayList<Receitas>() //inicializar arrays and adapters
    var arrSubCategory = ArrayList<Receitas>() //inicializar arrays and adapters
    var mainCategoryAdapter = MainCategoryAdapter() //inicializar arrays and adapters
    var subCategoryAdapter = SubCategoryAdapter()//inicializar arrays and adapters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.teste1)

        readStorageTask()


        //teste temporary
        arrMainCategory.add(Receitas(1, "Frango "))
        arrMainCategory.add(Receitas(2, "Bifes"))
        arrMainCategory.add(Receitas(3, "Carne"))
        arrMainCategory.add(Receitas(4, "KFC"))

        mainCategoryAdapter.setData(arrMainCategory)

        arrSubCategory.add(Receitas(2, "Frango a paneleiro"))
        arrSubCategory.add(Receitas(3, "Bifes"))
        arrSubCategory.add(Receitas(4, "Carne"))
        arrSubCategory.add(Receitas(5, "KFC"))

        subCategoryAdapter.setData(arrSubCategory)

        //temporary data
        val rvMainCategory = findViewById<RecyclerView>(R.id.rv_main_category)
        rvMainCategory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvMainCategory.adapter = mainCategoryAdapter

        val rvSubCategory = findViewById<RecyclerView>(R.id.rv_sub_category)
        rvSubCategory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvSubCategory.adapter = subCategoryAdapter

        //getCategorias para ir buscar data da api usando retrofit//
    }
        fun getCategorias() {
            val service = RetrofitClient.retrofitInstance!!.create(GetDataService::class.java)
            val call = service.getCategoryList()
            call.enqueue(object : Callback<Categoria> {
                override fun onFailure(call: Call<Categoria>, t: Throwable) {

                    Toast.makeText(this@MainActivity, "Algo Correu Mal", Toast.LENGTH_SHORT).show()

                }

                override fun onResponse(
                    call: Call<Categoria>,
                    response: Response<Categoria>
                ) {
                    insertDataIntoRoomDb(response.body())
                }

            })
        }

    /////////////////////////////////////////

    fun insertDataIntoRoomDb(categoria: Categoria?) {

    }

    private fun hasReadStoragePermission():Boolean{
        return EasyPermissions.hasPermissions(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun readStorageTask(){
        if (hasReadStoragePermission()){
            getCategorias()

        }else{
            EasyPermissions.requestPermissions(
                this,"Esta aplicação precisa de acesso ao seu armazenamento",
                READ_STORAGE_PERM,android.Manifest.permission.READ_EXTERNAL_STORAGE)

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }
    override fun onRationaleAccepted(requestCode: Int) {

    }

    override fun onRationaleDenied(requestCode: Int) {

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
                AppSettingsDialog.Builder(this).build().show()
            }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

    }

}



