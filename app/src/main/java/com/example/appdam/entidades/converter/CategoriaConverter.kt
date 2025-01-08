package com.example.appdam.entidades.converter

import androidx.room.TypeConverter
import com.example.appdam.entidades.Categoria
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CategoriaConverter {
    @TypeConverter
    fun fromCategoryList(categoria: List<Categoria>):String?{
        if (categoria == null){
            return (null)
    }
    else{
        val gson = Gson()
        val type = object : TypeToken<Categoria>(){

        }.type
            return gson.toJson(categoria,type)
    }
}

    @TypeConverter
    fun toCategoryList (categoriaString: String):List<Categoria>?{
        if (categoriaString == null){
            return (null)
    }
        else {
            val gson = Gson()
            val type = object :TypeToken<Categoria>(){
            }.type
            return gson.fromJson(categoriaString,type)
        }

    }
}