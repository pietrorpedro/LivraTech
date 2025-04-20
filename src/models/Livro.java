package models;

public class Livro {

    private int id;
    private String titulo;
    private String autor;
    private Double preco;
    private int nPaginas;
    private CategoriaLivro categoriaLivro;

    public Livro(
        String titulo,
        String autor,
        Double preco,
        int nPaginas,
        CategoriaLivro categoriaLivro
    ) {
        this.titulo = titulo;
        this.autor = autor;
        this.preco = preco;
        this.nPaginas = nPaginas;
        this.categoriaLivro = categoriaLivro;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        if (id >= 1) {
            this.id = id;
        }
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        if (titulo != null && !titulo.isEmpty()) {
            this.titulo = titulo;
        }
    }
    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        if (autor != null && !autor.isEmpty()) {
            this.autor = autor;
        }
    }
    public Double getPreco() {
        return preco;
    }
    public void setPreco(Double preco) {
        if (preco >= 0) {
            this.preco = preco;
        }
    }
    public int getnPaginas() {
        return nPaginas;
    }
    public void setnPaginas(int nPaginas) {
        if (nPaginas >= 1) {
            this.nPaginas = nPaginas;
        }
    }
    public CategoriaLivro getCategoriaLivro() {
        return categoriaLivro;
    }
    public void setCategoriaLivro(CategoriaLivro categoriaLivro) {
        if (categoriaLivro != null) {
            this.categoriaLivro = categoriaLivro;
        }
    }

}
