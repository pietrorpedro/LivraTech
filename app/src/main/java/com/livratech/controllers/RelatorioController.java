package com.livratech.controllers;

import java.util.List;

import com.livratech.dtos.RelatorioVendasDTO;
import com.livratech.dtos.TransacaoResponseDTO;
import com.livratech.services.ClienteService;
import com.livratech.services.TransacaoService;

import io.javalin.http.Handler;

public class RelatorioController {

    public static Handler getVendas = ctx -> {
        try {
            TransacaoService service = new TransacaoService();
            List<TransacaoResponseDTO> transacoes = service.buscarTodasTransacoesComoDTO();

            double valorTotal = 0;
            for (TransacaoResponseDTO dto : transacoes) {
                valorTotal += dto.valor;
            }

            RelatorioVendasDTO relatorio = new RelatorioVendasDTO(
                    transacoes,
                    valorTotal,
                    transacoes.size());

            ctx.json(relatorio);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("Erro ao obter transações");
        }
    };

    public static Handler quantidadeClientes = ctx -> {
        ClienteService clienteService = new ClienteService();
        int quantidade = clienteService.listarTodosClientes().size();
        ctx.result(String.valueOf(quantidade));
    };

}
