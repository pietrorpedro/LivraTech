package com.livratech.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.livratech.dtos.TransacaoResponseDTO;
import com.livratech.enums.TransacaoSituacao;
import com.livratech.enums.TransacaoTipo;
import com.livratech.models.Livro;
import com.livratech.models.Transacao;

public class TransacaoService {

    private static final String CAMINHO = "app\\src\\main\\java\\com\\livratech\\data\\transacoes.csv";
    private static final SimpleDateFormat formatoData = new SimpleDateFormat("dd-MM-yyyy");

    public void inserirTransacao(Transacao transacao) {
        try {
            Files.createDirectories(Paths.get("app\\src\\main\\java\\com\\livratech\\data"));

            boolean arquivoExiste = Files.exists(Paths.get(CAMINHO));

            Writer writer = new FileWriter(CAMINHO, true);
            if (!arquivoExiste) {
                writer.write(
                        "id;transacaoTipo;dataInicio;dataFim;valor;transacaoSituacao;clienteId;funcionarioId;livroIds\n");
            }

            String linha = String.join(";",
                    String.valueOf(transacao.getId()),
                    transacao.getTransacaoTipo().name(),
                    formatoData.format(transacao.getDataInicio()),
                    formatoData.format(transacao.getDataFim()),
                    String.valueOf(transacao.getValor()),
                    transacao.getTransacaoSituacao().name(),
                    String.valueOf(transacao.getCliente().getId()),
                    String.valueOf(transacao.getFuncionario().getId()),
                    getLivrosIdsComoString(transacao.getLivros()));

            writer.write(linha + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Transacao buscarTransacaoPorId(int id) {
        try {
            Reader reader = new FileReader(CAMINHO);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String linha;
            bufferedReader.readLine(); // Pular cabeçalho

            while ((linha = bufferedReader.readLine()) != null) {
                String[] dados = linha.split(";");
                int transacaoId = Integer.parseInt(dados[0]);

                if (transacaoId == id) {
                    Transacao transacao = new Transacao(
                            TransacaoTipo.valueOf(dados[1]),
                            formatoData.parse(dados[2]),
                            formatoData.parse(dados[3]),
                            Double.parseDouble(dados[4]),
                            TransacaoSituacao.valueOf(dados[5]),
                            new ClienteService().encontrarClientePorId(Integer.parseInt(dados[6])),
                            new FuncionarioService().encontrarFuncionarioPorId(Integer.parseInt(dados[7])),
                            getLivrosPorIds(dados[8]));
                    transacao.setId(transacaoId);
                    bufferedReader.close();
                    return transacao;
                }
            }

            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getLivrosIdsComoString(List<Livro> livros) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < livros.size(); i++) {
            builder.append(livros.get(i).getId());
            if (i < livros.size() - 1) {
                builder.append(",");
            }
        }
        return builder.toString();
    }

    private List<Livro> getLivrosPorIds(String ids) {
        LivroService livroService = new LivroService();
        List<Livro> livros = new ArrayList<>();
        for (String idStr : ids.split(",")) {
            Livro livro = livroService.encontrarLivroPorId(Integer.parseInt(idStr));
            if (livro != null) {
                livros.add(livro);
            }
        }
        return livros;
    }

    public List<TransacaoResponseDTO> buscarTransacoesPorCpf(String cpf) {
        List<TransacaoResponseDTO> transacoesEncontradas = new ArrayList<>();

        try (Reader reader = new FileReader(CAMINHO);
                BufferedReader bufferedReader = new BufferedReader(reader)) {

            ClienteService clienteService = new ClienteService();
            FuncionarioService funcionarioService = new FuncionarioService();

            String linha;
            bufferedReader.readLine(); // pular cabeçalho

            while ((linha = bufferedReader.readLine()) != null) {
                String[] dados = linha.split(";");
                int clienteId = Integer.parseInt(dados[6]);

                var cliente = clienteService.encontrarClientePorId(clienteId);
                if (cliente != null && cliente.getCPF().equals(cpf)) {
                    int id = Integer.parseInt(dados[0]);
                    String tipo = dados[1];
                    String dataInicio = dados[2];
                    String dataFim = dados[3];
                    double valor = Double.parseDouble(dados[4]);
                    String situacao = dados[5];
                    int funcionarioId = Integer.parseInt(dados[7]);
                    List<Integer> livrosIds = converterParaListaDeIds(dados[8]);

                    TransacaoResponseDTO dto = new TransacaoResponseDTO(
                            id,
                            tipo,
                            dataInicio,
                            dataFim,
                            valor,
                            situacao,
                            cpf,
                            funcionarioId,
                            livrosIds);
                    transacoesEncontradas.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return transacoesEncontradas;
    }

    private List<Integer> converterParaListaDeIds(String csv) {
        List<Integer> ids = new ArrayList<>();
        if (csv == null || csv.isBlank())
            return ids;

        String[] partes = csv.split(",");
        for (String parte : partes) {
            try {
                ids.add(Integer.parseInt(parte));
            } catch (NumberFormatException e) {
                // ignora valores inválidos
            }
        }
        return ids;
    }

}
