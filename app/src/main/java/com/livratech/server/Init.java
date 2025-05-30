package com.livratech.server;

import com.fasterxml.jackson.databind.ObjectMapper;
// import com.livratech.controllers.CarrinhoController;
import com.livratech.controllers.ClienteController;
import com.livratech.controllers.LivroController;
import com.livratech.controllers.RelatorioController;
import com.livratech.controllers.TransacaoController;

import io.javalin.Javalin;
import io.javalin.json.JavalinGson;
import io.javalin.json.JavalinJackson;

public class Init {

    public static void main(String[] args) {
    

        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson(new ObjectMapper(), false));
        });

        app.start();

        // CarrinhoController carrinhoController = new CarrinhoController();

        app.get("/clientes", ClienteController.getClientePorCPF);
        app.post("/clientes", ClienteController.postCliente);

        app.get("/transacoes", TransacaoController.getTransacoes);
        app.post("/transacoes", TransacaoController.postTransacao);
        app.post("/transacoes/devolucao", TransacaoController.postTransacaoDevolucao);

        app.post("/livros", LivroController.postLivro);

        app.get("/relatorio/vendas", RelatorioController.getVendas);
        app.get("/relatorio/clientes", RelatorioController.quantidadeClientes);

        // app.get("/carrinho", carrinhoController.listarCarrinho);
        // app.post("/carrinho", carrinhoController.adicionarLivro);
        // app.delete("/carrinho", carrinhoController.removerLivro);
    }
}
