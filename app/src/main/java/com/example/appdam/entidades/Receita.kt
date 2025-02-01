package com.example.appdam.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tbl_receita")
data class Receita(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val titulo: String,
    val fotourl: String?,
    val ingredientes: String,
    val preparo: String,
    @SerializedName("user_id") val userId: String
)
