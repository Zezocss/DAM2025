package com.example.appdam.entidades

import androidx.room.*
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.appdam.entidades.converter.CategoriaConverter
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Categoria")

data class Categoria(
    @PrimaryKey(autoGenerate = true)
    var id:Int,

    @ColumnInfo(name = "categoriasItens")
    @Expose
    @SerializedName("categorias")
    @TypeConverters(CategoriaConverter::class)
    val categoriasItens: List<CategoriaItens>? = null
)
