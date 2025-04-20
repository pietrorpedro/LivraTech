package services;

import models.Cliente;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Clientes {

    private static final String CAMINHO_ARQUIVO = "clientes.csv";
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

    public void adicionarCliente(Cliente cliente) {
        inicializarContador();
        cliente.setId(contadorId++);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO, true))) {

            writer.write(formatarCliente(cliente));
            writer.newLine();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Cliente buscarClientePorCpf(String cpf) {
        try {
            if (!Files.exists(Paths.get(CAMINHO_ARQUIVO))) return null;

            List<String> linhas = Files.readAllLines(Paths.get(CAMINHO_ARQUIVO));
            for (String linha : linhas) {
                String[] dados = linha.split(";");
                if (dados[2].equals(cpf)) {
                    Cliente cliente = new Cliente(dados[1], dados[2], dados[3], Boolean.parseBoolean(dados[4]));
                    cliente.setId(Integer.parseInt(dados[0]));
                    return cliente;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String formatarCliente(Cliente cliente) {
        return cliente.getId() + ";" +
                cliente.getNome() + ";" +
                cliente.getCPF() + ";" +
                cliente.getEndereco() + ";" +
                cliente.isClienteVip();
    }
}
