package com.example.appdam.receitasuser

import com.example.appdam.entidades.Receita

data class ResponseReceitas(
    val receitas: List<Receita> // Lista de receitas retornada do backend
)
