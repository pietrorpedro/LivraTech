package com.livratech.dtos;

import com.livratech.models.Livro;
import java.util.List;

public class CarrinhoResponse {
    private List<Livro> livros;
    private double valorTotal;

    public CarrinhoResponse(List<Livro> livros, double valorTotal) {
        this.livros = livros;
        this.valorTotal = valorTotal;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public double getValorTotal() {
        return valorTotal;
    }
}
