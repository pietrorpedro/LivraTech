package com.livratech.services;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import com.livratech.models.Carrinho;
import com.livratech.models.Livro;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

public class CarrinhoService {

    private static final String CAMINHO = "app\\src\\main\\java\\com\\livratech\\data\\carrinho.csv";

    public void inserirItemNoCarrinho(Livro livro) {
        Carrinho carrinho = carregarCarrinho();
        carrinho.getLivros().add(livro);
        carrinho.setValorTotal(carrinho.getValorTotal() + livro.getPreco());
        salvarCarrinho(carrinho);
    }

    public List<Livro> listarItensDoCarrinho() {
        return carregarCarrinho().getLivros();
    }

    public Carrinho obterCarrinho() {
        return carregarCarrinho();
    }

    public void removerItemDoCarrinho(int idLivro, int quantidade) {
        Carrinho carrinho = carregarCarrinho();
        List<Livro> livros = carrinho.getLivros();

        List<Livro> filtrados = livros.stream()
                .filter(l -> l.getId() == idLivro)
                .collect(Collectors.toList());

        if (filtrados.size() < quantidade) {
            System.out.println("Erro: Tentando remover mais livros do que existem no carrinho.");
            return;
        }

        double valorRemovido = 0.0;
        int removidos = 0;
        List<Livro> novaLista = new ArrayList<>();

        for (Livro l : livros) {
            if (l.getId() == idLivro && removidos < quantidade) {
                valorRemovido += l.getPreco();
                removidos++;
            } else {
                novaLista.add(l);
            }
        }

        carrinho.setLivros(novaLista);
        carrinho.setValorTotal(carrinho.getValorTotal() - valorRemovido);

        salvarCarrinho(carrinho);
    }

    public void limparCarrinho() {
        salvarCarrinho(new Carrinho(new ArrayList<>(), 0.0, null));
    }

    private Carrinho carregarCarrinho() {
        try {
            if (!Files.exists(Paths.get(CAMINHO)))
                return new Carrinho(new ArrayList<>(), 0.0, null);

            var parser = new CSVParserBuilder().withSeparator(';').build();
            List<String[]> linhas = new CSVReaderBuilder(new FileReader(CAMINHO))
                    .withSkipLines(1)
                    .withCSVParser(parser)
                    .build().readAll();

            if (linhas.isEmpty()) return new Carrinho(new ArrayList<>(), 0.0, null);

            String[] dados = linhas.get(0);
            String[] idLivros = dados[0].split(",");
            double valorTotal = Double.parseDouble(dados[1]);

            LivroService livroService = new LivroService();
            List<Livro> livros = Arrays.stream(idLivros)
                    .filter(s -> !s.isEmpty())
                    .map(id -> livroService.encontrarLivroPorId(Integer.parseInt(id)))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            return new Carrinho(livros, valorTotal, null);

        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return new Carrinho(new ArrayList<>(), 0.0, null);
        }
    }

    private void salvarCarrinho(Carrinho carrinho) {
        try {
            Files.createDirectories(Paths.get("app\\src\\main\\java\\com\\livratech\\data"));
            Writer writer = new FileWriter(CAMINHO);
            CSVWriter csvWriter = new CSVWriter(
                    writer,
                    ';',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);

            csvWriter.writeNext(new String[]{"livros", "valorTotal"});
            String livrosIds = carrinho.getLivros().stream()
                    .map(l -> String.valueOf(l.getId()))
                    .collect(Collectors.joining(","));
            csvWriter.writeNext(new String[]{livrosIds, String.valueOf(carrinho.getValorTotal())});
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
