package org.example.api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.models.Cliente;
import org.example.services.Clientes;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class ClienteHandler implements HttpHandler {

    private final Clientes clientesService = new Clientes();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String resposta = "";
        int statusCode = 200;

        if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            if (path.equals("/cliente/adicionar")) {
                boolean sucesso = adicionarCliente(exchange);
                resposta = gson.toJson(sucesso);
            } else {
                statusCode = 404;
                resposta = "Endpoint não encontrado.";
            }
        } else if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            if (path.startsWith("/cliente/buscar")) {
                Cliente cliente = buscarCliente(exchange);
                if (cliente != null) {
                    resposta = gson.toJson(cliente);
                } else {
                    statusCode = 404;
                    resposta = "Cliente não encontrado.";
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

    private boolean adicionarCliente(HttpExchange exchange) throws IOException {
        InputStreamReader reader = new InputStreamReader(exchange.getRequestBody());
        Cliente cliente = gson.fromJson(reader, Cliente.class);
        return clientesService.adicionarCliente(cliente);
    }

    private Cliente buscarCliente(HttpExchange exchange) throws IOException {
        String cpf = getQueryParam(exchange, "cpf");
        if (cpf == null) {
            throw new IOException("CPF não fornecido na query string");
        }
        return clientesService.buscarClientePorCpf(cpf);
    }

    private String getQueryParam(HttpExchange exchange, String param) {
        String query = exchange.getRequestURI().getQuery();
        if (query != null) {
            String[] params = query.split("&");
            for (String p : params) {
                String[] pair = p.split("=");
                if (pair[0].equals(param)) {
                    return pair[1];
                }
            }
        }
        return null;
    }
}
