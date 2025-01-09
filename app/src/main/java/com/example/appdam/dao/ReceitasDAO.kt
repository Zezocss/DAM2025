package com.example.appdam.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.example.appdam.entidades.CategoriaItens




@Dao
interface ReceitasDAO {
    @Query("SELECT * FROM categoriaItens ORDER BY id DESC")
    fun getAllCategory() : List<CategoriaItens>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategoria(categoriaItens: CategoriaItens?)

    @Query("DELETE FROM categoriaitens")
    fun clearDb()
}