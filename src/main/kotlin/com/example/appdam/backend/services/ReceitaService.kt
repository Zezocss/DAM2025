package com.example.appdam.backend.services

import com.example.appdam.backend.models.Receita
import com.example.appdam.backend.repositories.ReceitaRepository
import org.springframework.stereotype.Service

// ReceitaService.kt
@Service
class ReceitaService(private val receitaRepository: ReceitaRepository) {

    fun listarReceitasPorUtilizador(username: String): List<Receita> {
        return receitaRepository.findByUsername(username)
    }

    fun criarReceita(receita: Receita): Receita {
        return receitaRepository.save(receita)
    }

    fun atualizarReceita(id: Long, novaReceita: Receita): Receita {
        val receitaExistente = receitaRepository.findById(id).orElseThrow {
            IllegalArgumentException("Receita não encontrada!")
        }

        if (receitaExistente.username != novaReceita.username) {
            throw IllegalArgumentException("Não tem permissão para atualizar esta receita!")
        }

        receitaExistente.titulo = novaReceita.titulo
        receitaExistente.ingredientes = novaReceita.ingredientes
        receitaExistente.preparo = novaReceita.preparo
        receitaExistente.fotourl = novaReceita.fotourl ?: ""

        return receitaRepository.save(receitaExistente)
    }

    fun eliminarReceita(id: Long, username: String) {
        val receita = receitaRepository.findById(id).orElseThrow {
            IllegalArgumentException("Receita não encontrada!")
        }

        if (receita.username != username) {
            throw IllegalArgumentException("Não tem permissão para eliminar esta receita!")
        }

        receitaRepository.delete(receita)
    }
}

