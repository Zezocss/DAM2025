package com.example.appdam.backend.controllers

import com.example.appdam.backend.models.Receita
import com.example.appdam.backend.services.ReceitaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/receitas")
class ReceitaController(private val receitaService: ReceitaService) {

    @GetMapping
    fun listarReceitasPorUtilizador(@RequestParam username: String): ResponseEntity<List<Receita>> {
        val receitas = receitaService.listarReceitasPorUtilizador(username)
        return ResponseEntity.ok(receitas)
    }


   @PostMapping
fun criarReceita(@RequestBody receita: Receita): ResponseEntity<Receita> {
    println("Recebendo receita: ${receita.titulo}, Foto: ${receita.fotourl?.length ?: "SEM IMAGEM"}")
    val novaReceita = receitaService.criarReceita(receita)
    return ResponseEntity.status(HttpStatus.CREATED).body(novaReceita)
}

    }

    @PutMapping("/{id}")
    fun atualizarReceita(
        @PathVariable id: Long,
        @RequestBody novaReceita: Receita
    ): ResponseEntity<Receita> {
        val receitaAtualizada = receitaService.atualizarReceita(id, novaReceita)
        return ResponseEntity.ok(receitaAtualizada)
    }

    @DeleteMapping("/{id}")
    fun eliminarReceita(
        @PathVariable id: Long,
        @RequestParam username: String
    ): ResponseEntity<Void> {
        receitaService.eliminarReceita(id, username)
        return ResponseEntity.noContent().build()
    }
}
