package com.livratech.dtos;

import java.util.List;

public class TransacaoResponseDTO {
    public int id;
    public String transacaoTipo;
    public String dataInicio;
    public String dataFim;
    public Double valor;
    public String transacaoSituacao;
    public String cliente; // CPF
    public int funcionario; // ID
    public List<Integer> livros;

    public TransacaoResponseDTO(int id, String transacaoTipo, String dataInicio, String dataFim, Double valor,
            String transacaoSituacao, String cliente, int funcionario, List<Integer> livros) {
        this.id = id;
        this.transacaoTipo = transacaoTipo;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valor = valor;
        this.transacaoSituacao = transacaoSituacao;
        this.cliente = cliente;
        this.funcionario = funcionario;
        this.livros = livros;
    }
}
