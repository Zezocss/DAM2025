package com.example.appdam.backend.models

import jakarta.persistence.*

@Entity
@Table(name = "receitas")
data class Receita(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var titulo: String = "",

    @Column(columnDefinition = "TEXT", nullable = false)
    var ingredientes: String = "",

    @Column(columnDefinition = "TEXT", nullable = false)
    var preparo: String = "",
    @Column(columnDefinition = "TEXT", nullable = true)
    var fotourl: String? = null,

    var username: String = "" // O 'username' será enviado diretamente no corpo da requisição
)
