package com.example.appdam.receitasuser

import com.example.appdam.entidades.Receita


data class ResponseReceitas(
    val folha1: List<Receita> // Esse é o array que vem no JSON
)
