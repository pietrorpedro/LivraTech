package com.livratech.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.livratech.dtos.TransacaoDTO;
import com.livratech.dtos.TransacaoResponseDTO;
import com.livratech.enums.TransacaoSituacao;
import com.livratech.enums.TransacaoTipo;
import com.livratech.models.Cliente;
import com.livratech.models.Funcionario;
import com.livratech.models.Livro;
import com.livratech.models.Transacao;
import com.livratech.services.ClienteService;
import com.livratech.services.FuncionarioService;
import com.livratech.services.LivroService;
import com.livratech.services.TransacaoService;

import io.javalin.http.Handler;

public class TransacaoController {

    private static final SimpleDateFormat formattedData = new SimpleDateFormat("dd-MM-yyy");

    public static Handler getTransacoes = ctx -> {
        String cpf = ctx.queryParam("cpf");
        if (cpf == null || cpf.isEmpty()) {
            ctx.status(400).result("CPF Não informado");
            return;
        }

        TransacaoService transacaoService = new TransacaoService();
        List<TransacaoResponseDTO> dtos = transacaoService.buscarTransacoesPorCpf(cpf);

        ctx.status(200).json(dtos);
    };

    public static Handler postTransacaoDevolucao = ctx -> {
        try {
            TransacaoDTO dto = ctx.bodyAsClass(TransacaoDTO.class);

            ClienteService clienteService = new ClienteService();
            FuncionarioService funcionarioService = new FuncionarioService();
            LivroService livroService = new LivroService();
            TransacaoService transacaoService = new TransacaoService();

            Cliente cliente = clienteService.encontrarClientePorCPF(dto.cliente);
            if (cliente == null) {
                ctx.status(400).result("Cliente com CPF " + dto.cliente + " não encontrado");
                return;
            }

            Funcionario funcionario = funcionarioService.encontrarFuncionarioPorId(dto.funcionario);
            if (funcionario == null) {
                ctx.status(400).result("Funcionário com id " + dto.funcionario + " não encontrado");
                return;
            }

            List<Livro> livros = new ArrayList<>();
            for (int id : dto.livros) {
                Livro livro = livroService.encontrarLivroPorId(id);
                if (livro == null) {
                    ctx.status(400).result("Livro com id " + id + " não encontrado");
                    return;
                }
                livros.add(livro);
            }

            // Atualizar estoque após verificar todos os livros
            for (int id : dto.livros) {
                livroService.atualizarEstoque(id, true, 1);
            }

            TransacaoTipo tipo = TransacaoTipo.valueOf(dto.transacaoTipo.toUpperCase());
            TransacaoSituacao situacao = TransacaoSituacao.valueOf(dto.transacaoSituacao.toUpperCase());
            Date dataInicio = formattedData.parse(dto.dataInicio);
            Date dataFim = formattedData.parse(dto.dataFim);

            Transacao transacao = new Transacao(
                    tipo,
                    dataInicio,
                    dataFim,
                    dto.valor,
                    situacao,
                    cliente,
                    funcionario,
                    livros);

            transacaoService.inserirTransacao(transacao);

            ctx.status(201).result("Transação cadastrada com sucesso");
        } catch (Exception e) {
            ctx.status(400).result("Erro ao cadastrar a transação: " + e.getMessage());
        }
    };

    public static Handler postTransacao = ctx -> {
        try {
            TransacaoDTO dto = ctx.bodyAsClass(TransacaoDTO.class);

            ClienteService clienteService = new ClienteService();
            FuncionarioService funcionarioService = new FuncionarioService();
            LivroService livroService = new LivroService();
            TransacaoService transacaoService = new TransacaoService();

            Cliente cliente = clienteService.encontrarClientePorCPF(dto.cliente);
            if (cliente == null) {
                ctx.status(400).result("Cliente com CPF " + dto.cliente + " não encontrado");
                return;
            }

            Funcionario funcionario = funcionarioService.encontrarFuncionarioPorId(dto.funcionario);
            if (funcionario == null) {
                ctx.status(400).result("Funcionário com id " + dto.funcionario + " não encontrado");
                return;
            }

            List<Livro> livros = new ArrayList<>();
            for (int id : dto.livros) {
                Livro livro = livroService.encontrarLivroPorId(id);
                if (livro == null) {
                    ctx.status(400).result("Livro com id " + id + " não encontrado");
                    return;
                }
                livros.add(livro);
            }

            // Atualizar estoque após verificar todos os livros
            for (int id : dto.livros) {
                livroService.atualizarEstoque(id, false, 1);
            }

            TransacaoTipo tipo = TransacaoTipo.valueOf(dto.transacaoTipo.toUpperCase());
            TransacaoSituacao situacao = TransacaoSituacao.valueOf(dto.transacaoSituacao.toUpperCase());
            Date dataInicio = formattedData.parse(dto.dataInicio);
            Date dataFim = formattedData.parse(dto.dataFim);

            Transacao transacao = new Transacao(
                    tipo,
                    dataInicio,
                    dataFim,
                    dto.valor,
                    situacao,
                    cliente,
                    funcionario,
                    livros);

            transacaoService.inserirTransacao(transacao);

            ctx.status(201).result("Transação cadastrada com sucesso");
        } catch (Exception e) {
            ctx.status(400).result("Erro ao cadastrar a transação: " + e.getMessage());
        }
    };

}
