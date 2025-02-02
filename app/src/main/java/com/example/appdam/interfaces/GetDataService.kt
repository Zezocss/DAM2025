package com.example.appdam.interfaces

import com.example.appdam.entidades.Categoria
import com.example.appdam.entidades.Prato
import com.example.appdam.entidades.PratoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// Interface para definir as chamadas à API para obter categorias e pratos
interface GetDataService {
    // Obtém a lista de categorias disponíveis
    @GET("categories.php")
    fun getCategoryList(): Call<Categoria>

    // Obtém a lista de pratos de uma determinada categoria
    @GET("filter.php")
    fun getMealList(@Query("c")categoria: String): Call<Prato>

    // Obtém os detalhes de um prato específico através do ID
    @GET("lookup.php")
    fun getSpecificItem(@Query("i") id: String): Call<PratoResponse>



}