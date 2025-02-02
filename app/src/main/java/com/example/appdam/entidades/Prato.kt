package com.example.appdam.entidades


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.appdam.entidades.converter.PratosConverter
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Prato")
class Prato (
    @PrimaryKey(autoGenerate = true)
    var id:Int,

    @ColumnInfo(name = "meals")
    @Expose
    @SerializedName("meals")
    @TypeConverters(PratosConverter::class)
    val pratosItens: List<PratosItens>? = null

)



