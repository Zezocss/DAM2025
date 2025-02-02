package com.example.appdam.receitasuser.add

data class ReceitaRequest(
    val titulo: String,
    val fotourl: String?,
    val ingredientes: String,
    val preparo: String,
    val username: String// Nome do utilizador associado à receita
)
