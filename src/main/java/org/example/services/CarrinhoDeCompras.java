package org.example.services;

import org.example.models.Livro;
import org.example.models.LivroCarrinho;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CarrinhoDeCompras {

    private static final String CAMINHO_ARQUIVO = "carrinho.csv";
    private Estoque estoque = new Estoque();

    public boolean adicionarLivroAoCarrinho(int idLivro, int quantidade) {
        try {
            List<String> linhas = carregarLinhasCsvCarrinho();

            boolean livroExistente = false;

            for (int i = 0; i < linhas.size(); i++) {
                String[] dados = linhas.get(i).split(";");
                int id = Integer.parseInt(dados[0]);

                if (id == idLivro) {
                    livroExistente = true;
                    int quantidadeAtual = Integer.parseInt(dados[1]);
                    dados[1] = String.valueOf(quantidadeAtual + quantidade);
                    linhas.set(i, String.join(";", dados));
                    break;
                }
            }


            if (!livroExistente) {
                Livro livro = estoque.buscarLivroPorId(idLivro);
                if (livro != null) {
                    double valorTotalLivro = livro.getPreco() * quantidade;
                    linhas.add(idLivro + ";" + quantidade + ";" + valorTotalLivro);
                } else {
                    System.out.println("Livro não encontrado no estoque.");
                    return false;
                }
            }

            salvarLivrosCarrinho(linhas);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void removerLivroDoCarrinho(int idLivro) {
        try {
            List<String> linhas = carregarLinhasCsvCarrinho();
            boolean livroEncontrado = false;

            for (int i = 0; i < linhas.size(); i++) {
                String[] dados = linhas.get(i).split(";");
                int id = Integer.parseInt(dados[0]);

                if (id == idLivro) {
                    livroEncontrado = true;
                    linhas.remove(i);
                    break;
                }
            }

            if (livroEncontrado) {
                salvarLivrosCarrinho(linhas);
                System.out.println("Livro removido do carrinho.");
            } else {
                System.out.println("Livro não encontrado no carrinho.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<LivroCarrinho> obterLivrosDoCarrinho() {
        List<LivroCarrinho> livrosCarrinho = new ArrayList<>();
        try {
            List<String> linhas = carregarLinhasCsvCarrinho();

            for (String linha : linhas) {
                String[] dados = linha.split(";");
                int id = Integer.parseInt(dados[0]);
                int quantidade = Integer.parseInt(dados[1]);

                // Verifica o livro no estoque com o id
                Livro livro = estoque.buscarLivroPorId(id);
                if (livro != null) {
                    double valorTotal = livro.getPreco() * quantidade;
                    livrosCarrinho.add(new LivroCarrinho(livro, quantidade, valorTotal));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return livrosCarrinho;
    }

    public double calcularValorTotalCarrinho() {
        double valorTotalCarrinho = 0.0;

        List<LivroCarrinho> livrosCarrinho = obterLivrosDoCarrinho();

        for (LivroCarrinho item : livrosCarrinho) {
            valorTotalCarrinho += item.getValorTotal();
        }

        return valorTotalCarrinho;
    }

    public void limparCarrinho() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {
            // limpa o conteúdo deixando o arquivo vazio
            System.out.println("Carrinho limpo.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> carregarLinhasCsvCarrinho() throws IOException {
        List<String> linhas = new ArrayList<>();
        if (Files.exists(Paths.get(CAMINHO_ARQUIVO))) {
            linhas = Files.readAllLines(Paths.get(CAMINHO_ARQUIVO));
        }
        return linhas;
    }

    private void salvarLivrosCarrinho(List<String> linhas) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {
            for (String linha : linhas) {
                writer.write(linha);
                writer.newLine();
            }
        }
    }

    public boolean verificarCarrinhoVazio() {
        try {
            List<String> linhas = carregarLinhasCsvCarrinho();
            return linhas.isEmpty();
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }
}
