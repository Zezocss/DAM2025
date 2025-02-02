package com.example.appdam.backend.services

import com.example.appdam.backend.models.Receita
import com.example.appdam.backend.repositories.ReceitaRepository
import org.springframework.stereotype.Service

@Service
class ReceitaService(private val receitaRepository: ReceitaRepository) {

 fun listarReceitasPorUser(username: String): List<Receita> {
    return receitaRepository.findByUsername(username)
}

    // Criar uma receita
    fun criarReceita(receita: Receita): Receita {
        return receitaRepository.save(receita) // Salva a receita com o username associado
    }

    // Atualizar uma receita
    fun atualizarReceita(id: Long, novaReceita: Receita): Receita {
        val receitaExistente = receitaRepository.findById(id).orElseThrow {
            IllegalArgumentException("Receita não encontrada!")
        }

        // Atualiza os campos
        receitaExistente.titulo = novaReceita.titulo
        receitaExistente.ingredientes = novaReceita.ingredientes
        receitaExistente.preparo = novaReceita.preparo
        receitaExistente.fotourl = novaReceita.fotourl

        // Salva a receita atualizada
        return receitaRepository.save(receitaExistente)
    }

    // Excluir uma receita
    fun deletarReceita(id: Long) {
        val receita = receitaRepository.findById(id).orElseThrow {
            IllegalArgumentException("Receita não encontrada!")
        }
        receitaRepository.delete(receita)
    }
}
