package com.example.appdam.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.example.appdam.entidades.CategoriaItens
import com.example.appdam.entidades.Prato
import com.example.appdam.entidades.PratosItens


@Dao
interface ReceitasDAO {
    @Query("SELECT * FROM categoriaItens ORDER BY id DESC")
    fun getAllCategory() : List<CategoriaItens>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategoria(categoriaItens: CategoriaItens?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPrato(pratosItens: PratosItens?)

    @Query("DELETE FROM categoriaitens")
    fun clearDb()

    @Query("SELECT * FROM PratosItens WHERE categoryName = :categoryName ORDER BY id DESC")
    fun getSpecificPrato(categoryName:String) : List<PratosItens>
}