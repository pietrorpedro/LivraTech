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
import com.livratech.models.Cliente;
import com.livratech.models.Livro;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

public class CarrinhoService {

    private static final String CAMINHO = "app\\src\\main\\java\\com\\livratech\\data\\carrinho.csv";

    public void inserirItemNoCarrinho(String cpf, Livro livro) {
        try {
            Map<String, Carrinho> carrinhos = carregarCarrinhos();

            Carrinho carrinho = carrinhos.getOrDefault(cpf, new Carrinho(new ArrayList<>(), 0.0, new Cliente()));
            carrinho.getLivros().add(livro);
            carrinho.setValorTotal(carrinho.getValorTotal() + livro.getPreco());

            carrinhos.put(cpf, carrinho);
            salvarCarrinhos(carrinhos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Livro> listarItensDoCarrinho(String cpf) {
        Map<String, Carrinho> carrinhos = carregarCarrinhos();
        Carrinho carrinho = carrinhos.get(cpf);
        return (carrinho != null) ? carrinho.getLivros() : new ArrayList<>();
    }

    public Carrinho obterCarrinho(String cpf) {
        Map<String, Carrinho> carrinhos = carregarCarrinhos();
        return carrinhos.get(cpf);
    }

    public void removerItemDoCarrinho(String cpf, int idLivro, int quantidade) {
        Map<String, Carrinho> carrinhos = carregarCarrinhos();
        Carrinho carrinho = carrinhos.get(cpf);

        if (carrinho != null && quantidade > 0) {
            List<Livro> livros = carrinho.getLivros();

            List<Livro> livrosDoMesmoTipo = livros.stream()
                    .filter(l -> l.getId() == idLivro)
                    .collect(Collectors.toList());

            if (livrosDoMesmoTipo.size() < quantidade) {
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

            if (novaLista.isEmpty()) {
                carrinhos.remove(cpf);
            } else {
                carrinhos.put(cpf, carrinho);
            }

            salvarCarrinhos(carrinhos);
        }
    }

    public void limparCarrinho(String cpf) {
        Map<String, Carrinho> carrinhos = carregarCarrinhos();
        Carrinho carrinho = carrinhos.get(cpf);
        if (carrinho != null) {
            carrinho.setLivros(new ArrayList<>());
            carrinho.setValorTotal(0.0);
            salvarCarrinhos(carrinhos);
        }
    }

    private Map<String, Carrinho> carregarCarrinhos() {
        Map<String, Carrinho> mapa = new HashMap<>();
        try {
            if (!Files.exists(Paths.get(CAMINHO)))
                return mapa;

            var parser = new CSVParserBuilder().withSeparator(';').build();

            List<String[]> linhas = new CSVReaderBuilder(new FileReader(CAMINHO))
                    .withSkipLines(1)
                    .withCSVParser(parser)
                    .build().readAll();

            LivroService livroService = new LivroService();
            ClienteService clienteService = new ClienteService();

            for (String[] linha : linhas) {
                String cpf = linha[0];
                String[] idLivros = linha[1].split(",");
                double valorTotal = Double.parseDouble(linha[2]);

                List<Livro> livros = Arrays.stream(idLivros)
                        .filter(s -> !s.isEmpty())
                        .map(id -> livroService.encontrarLivroPorId(Integer.parseInt(id)))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                Cliente cliente = clienteService.encontrarClientePorCPF(cpf);
                Carrinho carrinho = new Carrinho(livros, valorTotal, cliente);

                mapa.put(cpf, carrinho);
            }

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return mapa;
    }

    private void salvarCarrinhos(Map<String, Carrinho> carrinhos) {
        try {
            Files.createDirectories(Paths.get("app\\src\\main\\java\\com\\livratech\\data"));
            Writer writer = new FileWriter(CAMINHO);
            CSVWriter csvWriter = new CSVWriter(
                    writer,
                    ';',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);

            csvWriter.writeNext(new String[] { "cpf", "livros", "valorTotal" });

            for (Map.Entry<String, Carrinho> entry : carrinhos.entrySet()) {
                String cpf = entry.getKey();
                Carrinho carrinho = entry.getValue();
                String livrosIds = carrinho.getLivros().stream()
                        .map(l -> String.valueOf(l.getId()))
                        .collect(Collectors.joining(","));
                csvWriter.writeNext(new String[] { cpf, livrosIds, String.valueOf(carrinho.getValorTotal()) });
            }

            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
