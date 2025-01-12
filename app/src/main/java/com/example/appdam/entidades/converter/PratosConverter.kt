package com.example.appdam.entidades.converter

import androidx.room.TypeConverter
import com.example.appdam.entidades.Categoria
import com.example.appdam.entidades.CategoriaItens
import com.example.appdam.entidades.PratosItens
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PratosConverter {
    @TypeConverter
    fun fromCategoryList(category: List<PratosItens>):String?{
        if (category == null){
            return (null)
        }else{
            val gson = Gson()
            val type = object : TypeToken<PratosItens>(){

            }.type
            return gson.toJson(category,type)
        }
    }

    @TypeConverter
    fun toCategoryList ( categoryString: String):List<PratosItens>?{
        if (categoryString == null){
            return (null)
        }else{
            val gson = Gson()
            val type = object :TypeToken<PratosItens>(){

            }.type
            return  gson.fromJson(categoryString,type)
        }
    }
}