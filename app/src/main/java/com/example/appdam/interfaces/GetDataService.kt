package com.example.appdam.interfaces

import com.example.appdam.entidades.Categoria
import retrofit2.Call
import retrofit2.http.GET

//trabalha com o link do retrofit e diz a qual ir buscar data
interface GetDataService {
    @GET("/categories.php")
    fun getCategoryList(): Call<Categoria>



}