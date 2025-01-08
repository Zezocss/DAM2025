package com.example.appdam.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "Receitas")
data class Receitas(
    @PrimaryKey(autoGenerate = true)
    var id:Int,

    @ColumnInfo(name = "dishName")
    var dishName:String
): Serializable

