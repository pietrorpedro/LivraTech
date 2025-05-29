package com.livratech.models;

import com.livratech.enums.CategoriaLivro;
import com.livratech.utils.CSVIDGenerator;
import com.opencsv.bean.CsvBindByPosition;

public class Livro {
    
    @CsvBindByPosition(position = 0)
    private int id;

    @CsvBindByPosition(position = 1)
    private String titulo;

    @CsvBindByPosition(position = 2)
    private String autor;

    @CsvBindByPosition(position = 3)
    private CategoriaLivro categoriaLivro;

    @CsvBindByPosition(position = 4)
    private Double preco;

    @CsvBindByPosition(position = 5)
    private int numeroPaginas;

    @CsvBindByPosition(position = 6)
    private int estoque;

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public CategoriaLivro getCategoriaLivro() {
        return categoriaLivro;
    }

    public void setCategoriaLivro(CategoriaLivro categoriaLivro) {
        this.categoriaLivro = categoriaLivro;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    public void setNumeroPaginas(int numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

    public Livro() {

    }

    public Livro(
        String titulo,
        String autor,
        CategoriaLivro categoriaLivro,
        Double preco,
        int numereroPaginas,
        int estoque
    ) {
        this.id = CSVIDGenerator.generate("app\\\\src\\\\main\\\\java\\\\com\\\\livratech\\\\data\\\\livros.csv");
        this.titulo = titulo;
        this.autor = autor;
        this.categoriaLivro = categoriaLivro;
        this.preco = preco;
        this.numeroPaginas = numereroPaginas;
        this.estoque = estoque;
    }
}
