package com.example.appdam.receitasuser.add

data class ReceitaRequest(
    val titulo: String,
    val fotourl: String?,
    val ingredientes: String,
    val preparo: String,
    val user_id: String
)
