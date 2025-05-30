package com.livratech.dtos;

import com.livratech.enums.CategoriaLivro;

public class LivroDTO {
    public String titulo;
    public String autor;
    public CategoriaLivro categoriaLivro;
    public Double preco;
    public int numeroPaginas;
    public int estoque;

    public LivroDTO() {}

    public LivroDTO(String titulo, String autor, CategoriaLivro categoriaLivro, Double preco, int numeroPaginas,
            int estoque) {
        this.titulo = titulo;
        this.autor = autor;
        this.categoriaLivro = categoriaLivro;
        this.preco = preco;
        this.numeroPaginas = numeroPaginas;
        this.estoque = estoque;
    }


}
