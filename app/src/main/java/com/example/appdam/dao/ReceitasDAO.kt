package com.example.appdam.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.example.appdam.entidades.Categoria
import com.example.appdam.entidades.Receitas



@Dao
interface ReceitasDAO {
    @get:Query("Select * FROM categoria ORDER BY id DESC")
    val getAllCategoria: List<Categoria>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategoria(categoria: Categoria)


}