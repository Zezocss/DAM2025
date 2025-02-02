package com.example.appdam.backend.controllers

import com.example.appdam.backend.models.Receita
import com.example.appdam.backend.services.ReceitaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/receitas")
class ReceitaController(private val receitaService: ReceitaService) {


    // Adicionar um novo endpoint para buscar receitas pelo username
    @GetMapping("/user/{username}")
    fun listarReceitasPorUser(@PathVariable username: String): ResponseEntity<List<Receita>> {
    val receitas = receitaService.listarReceitasPorUser(username)
    return ResponseEntity.ok(receitas)
}


    // Criar uma nova receita
    @PostMapping
    fun criarReceita(
        @RequestBody receita: Receita // Recebe os dados da receita, incluindo o username
    ): ResponseEntity<Receita> {
        println("DEBUG: Dados da receita recebidos: $receita")
        val username = receita.username // O username é parte do corpo da requisição
        println("DEBUG: Username associado à receita: $username")

        // Salva a receita no banco de dados, associando o username
        val novaReceita = receitaService.criarReceita(receita)

        return ResponseEntity.status(HttpStatus.CREATED).body(novaReceita)
    }

    // Atualizar uma receita
    @PutMapping("/{id}")
    fun atualizarReceita(
        @PathVariable id: Long, 
        @RequestBody novaReceita: Receita
    ): ResponseEntity<Receita> {
        val receitaAtualizada = receitaService.atualizarReceita(id, novaReceita)
        return ResponseEntity.ok(receitaAtualizada)
    }

    // Excluir uma receita
    @DeleteMapping("/{id}")
    fun deletarReceita(
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        receitaService.deletarReceita(id)
        return ResponseEntity.noContent().build()

        
    }
}
