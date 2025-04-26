package org.example.api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.models.Livro;
import org.example.models.Transacao;
import org.example.services.Estoque;
import org.example.services.Transacoes;
import enums.TransacaoSituacao;
import enums.TransacaoTipo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

public class TransacaoHandler implements HttpHandler {

    private final Transacoes transacoesService = new Transacoes();
    private final Estoque estoque = new Estoque();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String resposta = "";
        int statusCode = 200;

        if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            if (exchange.getRequestURI().getPath().equals("/transacao/criar")) {
                try {
                    InputStreamReader reader = new InputStreamReader(exchange.getRequestBody());
                    TransacaoRequest request = gson.fromJson(reader, TransacaoRequest.class);

                    // Criar transação
                    Transacao transacao = new Transacao(
                            request.getDataInicio(),
                            request.getDataFim(),
                            request.getValor(),
                            request.getSituacao(),
                            request.getTipo(),
                            request.getClienteCPF(),
                            request.getFuncionarioNome(),
                            request.getLivros()
                    );

                    // Adicionar transação com verificação
                    for (Livro livro : request.getLivros()) {
                        estoque.removerQuantidadeLivroEstoque(livro.getId(), 1);
                    }
                    boolean sucesso = transacoesService.adicionarTransacao(transacao);
                    if (sucesso) {
                        resposta = "Transação criada com sucesso.";
                    } else {
                        resposta = "Erro ao criar transação.";
                        statusCode = 500;
                    }
                } catch (Exception e) {
                    resposta = "Erro ao processar requisição: " + e.getMessage();
                    statusCode = 400;
                }
            } else {
                statusCode = 404;
                resposta = "Endpoint não encontrado.";
            }
        } else {
            statusCode = 405;
            resposta = "Método não permitido.";
        }

        exchange.sendResponseHeaders(statusCode, resposta.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(resposta.getBytes());
        }
    }

    private static class TransacaoRequest {
        private Date dataInicio;
        private Date dataFim;
        private Double valor;
        private TransacaoSituacao situacao;
        private TransacaoTipo tipo;
        private String clienteCPF;
        private String funcionarioNome;
        private List<Livro> livros;

        // Getters and Setters

        public Date getDataInicio() {
            return dataInicio;
        }

        public void setDataInicio(Date dataInicio) {
            this.dataInicio = dataInicio;
        }

        public Date getDataFim() {
            return dataFim;
        }

        public void setDataFim(Date dataFim) {
            this.dataFim = dataFim;
        }

        public Double getValor() {
            return valor;
        }

        public void setValor(Double valor) {
            this.valor = valor;
        }

        public TransacaoSituacao getSituacao() {
            return situacao;
        }

        public void setSituacao(TransacaoSituacao situacao) {
            this.situacao = situacao;
        }

        public TransacaoTipo getTipo() {
            return tipo;
        }

        public void setTipo(TransacaoTipo tipo) {
            this.tipo = tipo;
        }

        public String getClienteCPF() {
            return clienteCPF;
        }

        public void setClienteCPF(String clienteCPF) {
            this.clienteCPF = clienteCPF;
        }

        public String getFuncionarioNome() {
            return funcionarioNome;
        }

        public void setFuncionarioNome(String funcionarioNome) {
            this.funcionarioNome = funcionarioNome;
        }

        public List<Livro> getLivros() {
            return livros;
        }

        public void setLivros(List<Livro> livros) {
            this.livros = livros;
        }
    }
}
