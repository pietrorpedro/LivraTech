package com.livratech.controllers;

import com.livratech.dtos.LivroDTO;
import com.livratech.models.Livro;
import com.livratech.services.LivroService;

import io.javalin.http.Handler;

public class LivroController {

    public static Handler postLivro = ctx -> {
        try {
            LivroDTO dto = ctx.bodyAsClass(LivroDTO.class);

            Livro livro = new Livro(
                dto.titulo,
                dto.autor,
                dto.categoriaLivro,
                dto.preco,
                dto.numeroPaginas,
                dto.estoque
            );

            LivroService livroService = new LivroService();
            livroService.inserirLivro(livro);

            ctx.status(201).result("Livro cadastrado com sucesso.");
        } catch (Exception e) {
            ctx.status(400).result("Erro ao cadastrar livro: " + e.getMessage());
        }
    };
}
