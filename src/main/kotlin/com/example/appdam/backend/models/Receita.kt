package com.example.appdam.backend.models

import jakarta.persistence.*

@Entity
@Table(name = "receitas")
data class Receita(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var titulo: String = "",
    var ingredientes: String = "",
    var preparo: String = "",
    var fotourl: String? = null,
    var username: String = ""
)
