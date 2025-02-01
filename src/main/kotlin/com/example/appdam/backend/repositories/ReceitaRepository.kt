package com.example.appdam.backend.repositories

import com.example.appdam.backend.models.Receita
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReceitaRepository : JpaRepository<Receita, Long> {
    fun findByUsername(username: String): List<Receita>
}
