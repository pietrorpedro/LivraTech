package models;

import enums.TransacaoSituacao;
import enums.TransacaoTipo;

import java.util.Date;
import java.util.List;

public class Transacao {

    private int id;
    private Date dataInicio;
    private Date dataFim;
    private Double valor;
    private TransacaoSituacao situacao;
    private TransacaoTipo tipo;
    private int clienteId;
    private int funcionarioId;
    private List<Livro> livros;

    public Transacao(
            Date dataInicio,
            Date dataFim,
            Double valor,
            TransacaoSituacao situacao,
            TransacaoTipo tipo,
            int clienteId,
            int funcionarioId,
            List<Livro> livros
    ) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valor = valor;
        this.situacao = situacao;
        this.tipo = tipo;
        this.clienteId = clienteId;
        this.funcionarioId = funcionarioId;
        this.livros = livros;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }

    public int getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(int funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
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


