package com.example.appdam.interfaces

import com.example.appdam.entidades.Categoria
import com.example.appdam.entidades.Prato
import com.example.appdam.entidades.PratoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//trabalha com o link do retrofit e diz a qual ir buscar data
interface GetDataService {
    @GET("categories.php")
    fun getCategoryList(): Call<Categoria>

    @GET("filter.php")
    fun getMealList(@Query("c")categoria: String): Call<Prato>

    @GET("lookup.php")
    fun getSpecificItem(@Query("i") id: String): Call<PratoResponse>



}