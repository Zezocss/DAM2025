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

    @ColumnInfo(name = "categoriesItens")
    @Expose
    @SerializedName("categories")
    @TypeConverters(CategoriaConverter::class)
    val categories: List<CategoriaItens>? = null
)
