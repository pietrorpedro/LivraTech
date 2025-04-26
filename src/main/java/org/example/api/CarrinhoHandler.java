package org.example.api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.models.LivroCarrinho;
import org.example.services.CarrinhoDeCompras;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

public class CarrinhoHandler implements HttpHandler {

    private final CarrinhoDeCompras carrinho = new CarrinhoDeCompras();
    private final Gson gson = new Gson();

    private static class RequisicaoCarrinho {
        int idLivro;
        int quantidade;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (path.equals("/carrinho/adicionar") && exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            adicionarAoCarrinho(exchange);
        } else if (path.equals("/carrinho/obter") && exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            List<LivroCarrinho> itens = obterCarrinho();
            String resposta = gson.toJson(itens);
            exchange.sendResponseHeaders(200, resposta.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(resposta.getBytes());
            }
        } else if (path.equals("/carrinho/total") && exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            double total = obterTotalCarrinho();
            String resposta = gson.toJson(total);
            exchange.sendResponseHeaders(200, resposta.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(resposta.getBytes());
            }
        } else if (path.equals("/carrinho/limpar") && exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            boolean sucesso = limparCarrinho();
            String resposta = gson.toJson(sucesso);
            exchange.sendResponseHeaders(200, resposta.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(resposta.getBytes());
            }
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }

    private void adicionarAoCarrinho(HttpExchange exchange) throws IOException {
        InputStreamReader reader = new InputStreamReader(exchange.getRequestBody());
        RequisicaoCarrinho requisicao = gson.fromJson(reader, RequisicaoCarrinho.class);

        boolean sucesso = carrinho.adicionarLivroAoCarrinho(requisicao.idLivro, requisicao.quantidade);

        String resposta;
        if (sucesso) {
            resposta = "Livro adicionado ao carrinho com sucesso.";
            exchange.sendResponseHeaders(200, resposta.getBytes().length);
        } else {
            resposta = "Erro ao adicionar livro ao carrinho.";
            exchange.sendResponseHeaders(400, resposta.getBytes().length); // Código 400 para erro
        }

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(resposta.getBytes());
        }
    }

    private List<LivroCarrinho> obterCarrinho() {
        return carrinho.obterLivrosDoCarrinho();
    }

    private double obterTotalCarrinho() {
        return carrinho.calcularValorTotalCarrinho();
    }

    private boolean limparCarrinho() {
        carrinho.limparCarrinho();
        return true;
    }
}
