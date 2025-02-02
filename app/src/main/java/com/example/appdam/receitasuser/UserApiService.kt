package com.example.appdam.api

import com.example.appdam.entidades.Receita
import com.example.appdam.receitasuser.add.ReceitaRequest
import retrofit2.Call
import retrofit2.http.*

interface UserApiService {

    //Obtem lista de receitas de 1 utilizador em especifico
    @GET("receitas")
    fun getReceitas(@Query("username") username: String): Call<List<Receita>>

    //adiciona receita ao servidor

    @POST("receitas")
    fun addReceita(@Body receita: ReceitaRequest): Call<Void>

    //atualiza receita existente

    @PUT("receitas/{id}")
    fun updateReceita(@Path("id") id: Long, @Body receita: ReceitaRequest): Call<Void>

    //elimina receita existente
    @DELETE("receitas/{id}")
    fun deleteReceita(@Path("id") id: Long, @Query("username") username: String): Call<Void>
}
