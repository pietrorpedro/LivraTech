package org.example.services;

import enums.CategoriaLivro;
import org.example.models.Livro;

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

    public List<org.example.models.Livro> carregarLivros() {
        List<org.example.models.Livro> livros = new ArrayList<>();
        // se o arquivo não existir, retornar um array vazio
        if (!Files.exists(Paths.get(CAMINHO_ARQUIVO))) return livros;

        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {
            String linha;

            // transforma as linhas em livros e adiciona ao array
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                org.example.models.Livro livro = new org.example.models.Livro(
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

    public void adicionarLivro(org.example.models.Livro livro, int quantidade) {

        inicializarContador();
        livro.setId(contadorId++);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO, true))) {

            writer.write(formatarLivro(livro, quantidade));
            writer.newLine();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void removerQuantidadeLivroEstoque(int id, int quantidadeRemover) {
        List<Livro> livros = carregarLivros();
        boolean livroEncontrado = false;

        for (int i = 0; i < livros.size(); i++) {
            Livro livro = livros.get(i);
            if (livro.getId() == id) {
                livroEncontrado = true;

                // Carregar as linhas do arquivo CSV
                List<String> linhas = carregarLinhasCsv();

                // Pegar a linha correspondente ao livro
                String[] dados = linhas.get(i).split(";");
                int quantidadeAtual = Integer.parseInt(dados[6]);

                if (quantidadeAtual >= quantidadeRemover) {
                    // Subtrair a quantidade do livro
                    quantidadeAtual -= quantidadeRemover;
                    dados[6] = String.valueOf(quantidadeAtual); // Atualiza a quantidade no dado

                    // Substituir a linha no CSV
                    linhas.set(i, String.join(";", dados));

                    // Salvar as linhas modificadas de volta no arquivo
                    salvarLivros(linhas);

                    System.out.println("Quantidade do livro atualizada com sucesso.");
                } else {
                    System.out.println("Quantidade insuficiente no estoque.");
                }
                break;
            }
        }

        if (!livroEncontrado) {
            System.out.println("Livro não encontrado.");
        }
    }

    public org.example.models.Livro buscarLivroPorId(int idLivro) {
        List<org.example.models.Livro> livros = carregarLivros();
        for (org.example.models.Livro livro : livros) {
            if (livro.getId() == idLivro) {
                return livro;
            }
        }
        return null;
    }

    private List<String> carregarLinhasCsv() {
        List<String> linhas = new ArrayList<>();
        try {
            if (Files.exists(Paths.get(CAMINHO_ARQUIVO))) {
                linhas = Files.readAllLines(Paths.get(CAMINHO_ARQUIVO));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return linhas;
    }
    private void salvarLivros(List<String> linhas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {
            for (String linha : linhas) {
                writer.write(linha);
                writer.newLine();
            }
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    private String formatarLivro(org.example.models.Livro livro, int quantidade) {
        return livro.getId() + ";" +
                livro.getTitulo() + ";" +
                livro.getAutor() + ";" +
                livro.getPreco() + ";" +
                livro.getnPaginas() + ";" +
                livro.getCategoriaLivro() + ";" +
                quantidade;
    }
}



