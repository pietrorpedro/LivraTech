package com.livratech.models;

import java.util.Date;
import java.util.List;

import com.livratech.enums.TransacaoSituacao;
import com.livratech.enums.TransacaoTipo;
import com.livratech.utils.CSVIDGenerator;
import com.opencsv.bean.CsvBindByPosition;

public class Transacao {
    @CsvBindByPosition(position = 0)
    private int id;

    @CsvBindByPosition(position = 1)
    private TransacaoTipo transacaoTipo;

    @CsvBindByPosition(position = 2)
    private Date dataInicio;
    
    @CsvBindByPosition(position = 3)
    private Date dataFim;

    @CsvBindByPosition(position = 4)
    private Double valor;

    @CsvBindByPosition(position = 5)
    private TransacaoSituacao transacaoSituacao;

    @CsvBindByPosition(position = 6)
    private Cliente cliente;

    @CsvBindByPosition(position = 7)
    private Funcionario funcionario;

    @CsvBindByPosition(position = 8)
    private List<Livro> livros;

    public Transacao() {}
    
    public Transacao(TransacaoTipo transacaoTipo, Date dataInicio, Date dataFim, Double valor,
            TransacaoSituacao transacaoSituacao, Cliente cliente, Funcionario funcionario, List<Livro> livros) {
        this.transacaoTipo = transacaoTipo;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valor = valor;
        this.transacaoSituacao = transacaoSituacao;
        this.cliente = cliente;
        this.funcionario = funcionario;
        this.livros = livros;
        this.id = CSVIDGenerator.generate("app\\src\\main\\java\\com\\livratech\\data\\transacoes.csv");
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public TransacaoTipo getTransacaoTipo() {
        return transacaoTipo;
    }
    public void setTransacaoTipo(TransacaoTipo transacaoTipo) {
        this.transacaoTipo = transacaoTipo;
    }
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
    public TransacaoSituacao getTransacaoSituacao() {
        return transacaoSituacao;
    }
    public void setTransacaoSituacao(TransacaoSituacao transacaoSituacao) {
        this.transacaoSituacao = transacaoSituacao;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public Funcionario getFuncionario() {
        return funcionario;
    }
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
    public List<Livro> getLivros() {
        return livros;
    }
    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }
}
