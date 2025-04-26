package org.example.models;

public class LivroCarrinho {
    private Livro livro;
    private int quantidade;
    private double valorTotal;

    public LivroCarrinho(Livro livro, int quantidade, double valorTotal) {
        this.livro = livro;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
    }

    public Livro getLivro() {
        return livro;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getValorTotal() {
        return valorTotal;
    }
}

