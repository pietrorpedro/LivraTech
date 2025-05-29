package com.livratech.controllers;

import java.util.List;
import java.util.Map;

import com.livratech.dtos.CarrinhoResponse;
import com.livratech.models.Carrinho;
import com.livratech.models.Livro;
import com.livratech.services.CarrinhoService;
import com.livratech.services.LivroService;

import io.javalin.http.Handler;

public class CarrinhoController {
    CarrinhoService carrinhoService = new CarrinhoService();
    LivroService livroService = new LivroService();

    // GET /carrinho/?cpf=
    public Handler listarCarrinho = ctx -> {
        String cpf = ctx.queryParam("cpf");
        Carrinho carrinho = carrinhoService.obterCarrinho(cpf);

        if (carrinho == null) {
            ctx.status(404).result("Carrinho não encontrado");
            return;
        }

        CarrinhoResponse response = new CarrinhoResponse(carrinho.getLivros(), carrinho.getValorTotal());
        ctx.json(response);
    };

    // POST /carrinho
    public Handler adicionarLivro = ctx -> {
        // Extrair JSON do corpo
        Map<String, Object> body = ctx.bodyAsClass(Map.class);

        String cpf = (String) body.get("cpf");
        int idLivro = (int) body.get("idLivro");
        int quantidade = (int) body.get("quantidade");

        Livro livro = livroService.encontrarLivroPorId(idLivro);
        if (livro == null) {
            ctx.status(404).result("Livro não encontrado");
            return;
        }

        for (int i = 0; i < quantidade; i++) {
            carrinhoService.inserirItemNoCarrinho(cpf, livro);
        }

        ctx.status(200).result("Livro(s) adicionado(s) ao carrinho");
    };

    // DELETE /carrinho
    public Handler removerLivro = ctx -> {
        String cpf = ctx.queryParam("cpf");
        int idLivro = Integer.parseInt(ctx.queryParam("idLivro"));
        int quantidade = Integer.parseInt(ctx.queryParam("quantidade"));

        carrinhoService.removerItemDoCarrinho(cpf, idLivro, quantidade);
        ctx.status(200).result("Livro(s) removido(s) do carrinho");
    };
}
