package com.livratech.models;

import java.util.List;

public class Carrinho {
    private int id;
    private List<Livro> livros;
    private double valorTotal;
    private Cliente cliente;
    
    public Carrinho(List<Livro> livros, double valorTotal, Cliente cliente) {
        this.livros = livros;
        this.valorTotal = valorTotal;
        this.cliente = cliente;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public List<Livro> getLivros() {
        return livros;
    }
    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }
    public double getValorTotal() {
        return valorTotal;
    }
    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
