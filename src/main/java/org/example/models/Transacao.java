package org.example.models;

import enums.TransacaoSituacao;
import enums.TransacaoTipo;
import org.example.models.Livro;

import java.util.Date;
import java.util.List;

public class Transacao {

    private int id;
    private Date dataInicio;
    private Date dataFim;
    private Double valor;
    private TransacaoSituacao situacao;
    private TransacaoTipo tipo;
    private String clienteCPF;
    private String funcionarioNome;
    private List<Livro> livros;

    public Transacao(
            Date dataInicio,
            Date dataFim,
            Double valor,
            TransacaoSituacao situacao,
            TransacaoTipo tipo,
            String clienteCPF,
            String funcionarioNome,
            List<Livro> livros
    ) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valor = valor;
        this.situacao = situacao;
        this.tipo = tipo;
        this.clienteCPF = clienteCPF;
        this.funcionarioNome = funcionarioNome;
        this.livros = livros;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }

    public String getFuncionarioNome() {
        return funcionarioNome;
    }

    public void setFuncionarioNome(String funcionarioNome) {
        this.funcionarioNome = funcionarioNome;
    }

    public String getClienteCPF() {
        return clienteCPF;
    }

    public void setClienteCPF(String clienteCPF) {
        this.clienteCPF = clienteCPF;
    }

    public TransacaoTipo getTipo() {
        return tipo;
    }

    public void setTipo(TransacaoTipo tipo) {
        this.tipo = tipo;
    }

    public TransacaoSituacao getSituacao() {
        return situacao;
    }

    public void setSituacao(TransacaoSituacao situacao) {
        this.situacao = situacao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id >= 1) {
            this.id = id;
        }
    }
}
