package com.example.appdam.api

import com.example.appdam.entidades.Receita
import com.example.appdam.receitasuser.add.ReceitaRequest
import retrofit2.Call
import retrofit2.http.*

interface UserApiService {


    @GET("receitas")
    fun getReceitas(@Query("username") username: String): Call<List<Receita>>

    @POST("receitas")
    fun addReceita(@Body receita: ReceitaRequest): Call<Void>

    @PUT("receitas/{id}")
    fun updateReceita(@Path("id") id: Long, @Body receita: ReceitaRequest): Call<Void>

    @DELETE("receitas/{id}")
    fun deleteReceita(@Path("id") id: Long, @Query("username") username: String): Call<Void>
}
