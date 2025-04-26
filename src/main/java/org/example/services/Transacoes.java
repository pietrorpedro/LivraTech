package org.example.services;

import org.example.models.Livro;
import org.example.models.Transacao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;

public class Transacoes {

    private static final String CAMINHO_ARQUIVO = "transacoes.csv";
    private static int contadorId = 1;

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean adicionarTransacao(Transacao transacao) {
        inicializarContador();
        transacao.setId(contadorId++);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO, true))) {

            writer.write(formatarTransacao(transacao));
            writer.newLine();

            return true;

        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private String formatarTransacao(Transacao transacao) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String livrosIds = transacao.getLivros().stream()
                .map(Livro::getId)
                .map(String::valueOf)
                .reduce((a, b) -> a + "," + b)
                .orElse("");

        return transacao.getId() + ";" +
                sdf.format(transacao.getDataInicio()) + ";" +
                (transacao.getDataFim() != null ? sdf.format(transacao.getDataFim()) : "") + ";" +
                transacao.getValor() + ";" +
                transacao.getSituacao() + ";" +
                transacao.getTipo() + ";" +
                transacao.getClienteCPF() + ";" +
                transacao.getFuncionarioNome() + ";" +
                livrosIds;
    }
}
