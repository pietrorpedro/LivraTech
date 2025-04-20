package models;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Estoque {

    private static final String CAMINHO_ARQUIVO = "estoque.csv";
    private static int contadorId = 1;

    // pega o id do ultimo livro e adiciona ao contador para ids dinamicos
    private void inicializarContador() {
        try {

            if (Files.exists(Paths.get(CAMINHO_ARQUIVO))) {
                List<String> linhas = Files.readAllLines(Paths.get(CAMINHO_ARQUIVO));
                if (!linhas.isEmpty()) {
                    String ultimaLinha = linhas.get(linhas.size() - 1);
                    String[] dados = ultimaLinha.split(";");
                    contadorId = Integer.parseInt(dados[0]) + 1;
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public List<Livro> carregarLivros() {
        List<Livro> livros = new ArrayList<>();
        // se o arquivo não existir, retornar um array vazio
        if (!Files.exists(Paths.get(CAMINHO_ARQUIVO))) return livros;

        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {
            String linha;

            // transforma as linhas em livros e adiciona ao array
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                Livro livro = new Livro(
                        // ignorar id
                        dados[1],
                        dados[2],
                        Double.parseDouble(dados[3]),
                        Integer.parseInt(dados[4]),
                        CategoriaLivro.valueOf(dados[5])
                );

                livro.setId(Integer.parseInt(dados[0]));
                livros.add(livro);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return livros;
    }

    public void adicionarLivro(Livro livro) {

        inicializarContador();
        livro.setId(contadorId++);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO, true))) {

            writer.write(formatarLivro(livro));
            writer.newLine();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private String formatarLivro(Livro livro) {
        return livro.getId() + ";" +
                livro.getTitulo() + ";" +
                livro.getAutor() + ";" +
                livro.getPreco() + ";" +
                livro.getnPaginas() + ";" +
                livro.getCategoriaLivro();
    }
}



