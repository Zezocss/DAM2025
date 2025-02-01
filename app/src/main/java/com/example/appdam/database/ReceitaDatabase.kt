package com.example.appdam.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appdam.dao.ReceitaDao
import com.example.appdam.entidades.Receita

@Database(entities = [Receita::class], version = 1, exportSchema = false)
abstract class ReceitaDatabase : RoomDatabase() {

    abstract fun receitaDao(): ReceitaDao

    companion object {
        @Volatile
        private var INSTANCE: ReceitaDatabase? = null

        fun getDatabase(context: Context): ReceitaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReceitaDatabase::class.java,
                    "receita_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
