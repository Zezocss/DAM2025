package com.example.appdam.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appdam.dao.ReceitasDAO
import com.example.appdam.entidades.Categoria
import com.example.appdam.entidades.CategoriaItens
import com.example.appdam.entidades.Prato
import com.example.appdam.entidades.PratosItens
import com.example.appdam.entidades.Receitas
import com.example.appdam.entidades.converter.CategoriaConverter
import com.example.appdam.entidades.converter.PratosConverter

@Database(entities = [Receitas::class,CategoriaItens::class,Categoria::class,Prato::class,PratosItens::class], version = 1, exportSchema = false)
@TypeConverters(CategoriaConverter::class,PratosConverter::class)
abstract class ReceitasDatabase: RoomDatabase() {

    companion object{

        var receitasDatabase:ReceitasDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): ReceitasDatabase{
            if (receitasDatabase == null){
                receitasDatabase = Room.databaseBuilder(
                    context,
                    ReceitasDatabase::class.java,
                    "receitas.db"
                ).build()

            }
            return receitasDatabase!!
        }
    }
    abstract fun receitasDao():ReceitasDAO
}