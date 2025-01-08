package com.example.appdam.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.example.appdam.entidades.Receitas



@Dao
interface ReceitasDAO {
    @get:Query("Select * FROM receitas ORDER BY id DESC")
    val allReceitas: List<Receitas>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReceitas(receitas: Receitas)
}