package com.example.appdam.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tbl_receita")
data class Receita(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val titulo: String,
    var fotourl: String? = null,
    val ingredientes: String,
    val preparo: String,
    @SerializedName("username") val username: String
)
