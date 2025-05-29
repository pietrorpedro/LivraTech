package com.livratech.controllers;

import com.livratech.models.Cliente;
import com.livratech.services.ClienteService;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class ClienteController {

    public static Handler getClientePorCPF = ctx -> {
        String cpf = ctx.queryParam("cpf");
        if (cpf == null || cpf.isEmpty()) {
            ctx.status(400).result("CPF Não informado");
            return;
        }

        ClienteService service = new ClienteService();
        Cliente cliente = service.encontrarClientePorCPF(cpf);

        if (cliente == null) {
            ctx.status(400).result("Cliente não encontrado");
            return;
        }

        ctx.json(cliente);
    };

    public static Handler postCliente = ctx -> {
        try {
            Cliente cliente = ctx.bodyAsClass(Cliente.class);
            ClienteService service = new ClienteService();

            Cliente existente = service.encontrarClientePorCPF(cliente.getCPF());
            if (existente != null) {
                ctx.status(409).result("Cliente já cadastrado");
                return;
            }

            service.inserirCliente(cliente);
            ctx.status(201).result("Cliente cadastrado com sucesso");
        } catch (Exception e) {
            ctx.status(400).result("Erro ao cadastrar o cliente: " + e.getMessage());
        }
    };
}
