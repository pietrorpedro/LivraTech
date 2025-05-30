package com.livratech.dtos;

import java.util.List;

public class RelatorioVendasDTO {
    public List<TransacaoResponseDTO> transacoes;
    public double valorTotalVendas;
    public int quantidadeTotalTransacoes;

    public RelatorioVendasDTO(List<TransacaoResponseDTO> transacoes, double valorTotalVendas, int quantidadeTotalTransacoes) {
        this.transacoes = transacoes;
        this.valorTotalVendas = valorTotalVendas;
        this.quantidadeTotalTransacoes = quantidadeTotalTransacoes;
    }
}
