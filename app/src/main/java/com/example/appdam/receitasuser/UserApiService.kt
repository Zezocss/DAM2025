package com.example.appdam.receitasuser

import com.example.appdam.entidades.Receita
import com.example.appdam.receitasuser.add.ReceitaRequest
import com.example.appdam.receitasuser.add.ResponseAddReceitas
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApiService {

    // Endpoint para listar todas as receitas
    @GET("receitas")
    fun getReceitas(): Call<ResponseReceitas>


    @POST("receitas")
    fun addReceita(@Body body: Map<String, ReceitaRequest>): Call<ResponseAddReceitas>


        @PUT("receitas/{id}")
        fun updateReceita(@Path("id") id: Long, @Body receita: Map<String, Receita>): Call<Void>

        @DELETE("receitas/{id}")
        fun deleteReceita(@Path("id") id: Long): Call<Void>
    }
