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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.Locale
import java.util.Locale.Category


class MainActivity : BaseActivity() {
    var arrMainCategory = ArrayList<Receitas>() //inicializar arrays and adapters
    var arrSubCategory = ArrayList<Receitas>() //inicializar arrays and adapters
    var mainCategoryAdapter = MainCategoryAdapter() //inicializar arrays and adapters
    var subCategoryAdapter = SubCategoryAdapter()//inicializar arrays and adapters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.teste1)


        //teste temporary
        arrMainCategory.add(Receitas(1,"Frango "))
        arrMainCategory.add(Receitas(2,"Bifes"))
        arrMainCategory.add(Receitas(3,"Carne"))
        arrMainCategory.add(Receitas(4,"KFC"))

        mainCategoryAdapter.setData(arrMainCategory)

        arrSubCategory.add(Receitas(2,"Frango a paneleiro"))
        arrSubCategory.add(Receitas(3,"Bifes"))
        arrSubCategory.add(Receitas(4,"Carne"))
        arrSubCategory.add(Receitas(5,"KFC"))

        subCategoryAdapter.setData(arrSubCategory)

        //temporary data
        val rvMainCategory = findViewById<RecyclerView>(R.id.rv_main_category)
        rvMainCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvMainCategory.adapter = mainCategoryAdapter

        val rvSubCategory = findViewById<RecyclerView>(R.id.rv_sub_category)
        rvSubCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvSubCategory.adapter = subCategoryAdapter

 //getCategorias para ir buscar data da api usando retrofit
    fun getCategorias(){
        val service = RetrofitClient.retrofitInstance.create(GetDataService::class.java)
        val call = service.getCategoryList()
        call.enqueue(object : Callback<List<Categoria>> {

            override fun onFailure(p0: Call<List<Categoria>>, p1: Throwable) {

                Toast.makeText(this@MainActivity, "Algo Correu Mal", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(
                call: Call<List<Categoria>>,
                response: Response<List<Categoria>>
            ) {
                insertDataIntoRoomDb(response.body())
            }

        })
 }
}
    fun insertDataIntoRoomDb(categoria: List<Categoria>?){

    }

}



