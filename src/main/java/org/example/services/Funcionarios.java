package services;

import models.Funcionario;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Funcionarios {

    private static final String CAMINHO_ARQUIVO = "funcionarios.csv";
    private static int contadorId = 1;

    // mesma logica do estoque de livros
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

    public void adicionarFuncionario(Funcionario funcionario) {
        inicializarContador();
        funcionario.setId(contadorId++);

        try (BufferedWriter writer = new BufferedWriter((new FileWriter(CAMINHO_ARQUIVO, true)))) {

            writer.write(formatarFuncionario(funcionario));
            writer.newLine();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // mesma logica de formatação para csv
    private String formatarFuncionario(Funcionario funcionario) {
        return funcionario.getId() + ";" +
                funcionario.getNome() + ";" +
                funcionario.getSenha() + ";" +
                funcionario.getFuncionarioTipo();
    }

    public Funcionario autenticar(String nome, String senha) {
        try {

            if (Files.exists(Paths.get(CAMINHO_ARQUIVO))) {
                List<String> linhas = Files.readAllLines(Paths.get(CAMINHO_ARQUIVO));

                // verifica linha por linha se o login esta certo
                for (String linha : linhas) {
                    String[] dados = linha.split(";");
                    if (dados[1].equals(nome) && dados[2].equals(senha)) {
                        Funcionario funcionario = new Funcionario(dados[1], dados[2], enums.FuncionarioTipo.valueOf(dados[3]));
                        funcionario.setId(Integer.parseInt(dados[0]));

                        return funcionario;
                    }
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
