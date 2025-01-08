package com.example.appdam.entidades.converter

import androidx.room.TypeConverter
import com.example.appdam.entidades.Categoria
import com.example.appdam.entidades.CategoriaItens
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CategoriaConverter {
    @TypeConverter
    fun fromCategoryList(category: List<CategoriaItens>):String?{
        if (category == null){
            return (null)
        }else{
            val gson = Gson()
            val type = object : TypeToken<CategoriaItens>(){

            }.type
            return gson.toJson(category,type)
        }
    }

    @TypeConverter
    fun toCategoryList ( categoryString: String):List<CategoriaItens>?{
        if (categoryString == null){
            return (null)
        }else{
            val gson = Gson()
            val type = object :TypeToken<CategoriaItens>(){

            }.type
            return  gson.fromJson(categoryString,type)
        }
    }
}