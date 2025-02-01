package com.example.appdam.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import com.example.appdam.entidades.Receita

@Dao
interface ReceitaDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserir(receita: Receita): Long

    @Update
    fun atualizar(receita: Receita): Int

    @Delete
    fun eliminar(receita: Receita): Int

    @Query("SELECT * FROM tbl_receita ORDER BY id DESC")
    fun listarTodas(): List<Receita>

    @Query("SELECT * FROM tbl_receita WHERE id = :id LIMIT 1")
    fun obterPorId(id: Long): Receita?
}
