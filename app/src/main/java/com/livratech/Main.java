package com.livratech;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.livratech.dtos.TransacaoDTO;
import com.livratech.enums.CategoriaLivro;
import com.livratech.enums.FuncionarioTipo;
import com.livratech.enums.TransacaoSituacao;
import com.livratech.enums.TransacaoTipo;
import com.livratech.models.Carrinho;
import com.livratech.models.Cliente;
import com.livratech.models.Funcionario;
import com.livratech.models.Livro;
import com.livratech.models.Transacao;
import com.livratech.server.Init;
import com.livratech.services.CarrinhoService;
import com.livratech.services.ClienteService;
import com.livratech.services.FuncionarioService;
import com.livratech.services.LivroService;
import com.livratech.services.TransacaoService;
import com.livratech.utils.CSVIDGenerator;
import com.livratech.utils.ValidateUser;
import com.mashape.unirest.http.Unirest;

public class Main {

    private static Funcionario funcionarioCadastrado;
    private static final Scanner scanner = new Scanner(System.in);
    private static final ClienteService clienteService = new ClienteService();
    private static final CarrinhoService carrinhoService = new CarrinhoService();
    private static final LivroService livroService = new LivroService();
    private static final TransacaoService transacaoService = new TransacaoService();

    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        Init.main(args);

        System.out.println("\n\n\n\n\n\n");
        System.out.println("--------------------");
        System.out.println("Bem-vindo(a) ao sistema da LivraTech!");
        System.out.println("Por favor, realize seu login:");
        System.out.println("--------------------");

        login();
        System.out.println("Bem-vindo(a) " + funcionarioCadastrado.getNome());
        System.out.println("Seu cargo: " + funcionarioCadastrado.getFuncionarioTipo());
        opcoes();
    }

    public static void login() {

        while (true) {
            System.out.println("Nome de usuário:");
            String nome = scanner.nextLine();

            System.out.println("Senha:");
            String senha = scanner.nextLine();

            Funcionario funcionario = ValidateUser.validate(nome, senha);
            if (funcionario != null) {
                funcionarioCadastrado = funcionario;
                break;
            } else {
                System.out.println("Dados incorretos, tente novamente. \n");
            }
        }
    }

    public static void opcoes() {

        while (true) {
            System.out.println("\n\n\n\n\n\n");
            System.out.println("--------------------");
            System.out.println("Selecione uma das opções");
            System.out.println("--------------------");

            System.out.println("(1) Realizar venda");
            System.out.println("(2) Realizar devolução");
            System.out.println("(3) Realizar troca");

            String escolha = scanner.nextLine();

            switch (escolha) {
                case "1":
                    realizarVenda();
                    break;
                case "2":
                    realizarDevolucao();
                    break;
                case "3":

                    break;

                default:
                    System.out.println("Opção inválida, tente novamente.");
                    break;
            }
        }
    }

    private static void realizarVenda() {
        while (true) {
            System.out.println("\n");
            System.out.println("--------------------");
            System.out.println("Realizar venda");
            System.out.println("Escolha uma das opções");
            System.out.println("--------------------");

            System.out.println("(1) Inserir livro no carrinho");
            System.out.println("(2) Visualizar carrinho");
            System.out.println("(3) Remover um item do carrinho");
            System.out.println("(4) Finalizar venda");
            System.out.println("(0) Voltar");

            String escolha = scanner.nextLine();

            switch (escolha) {
                case "1":
                    try {
                        System.out.println("Insira o id do livro");
                        int idLivro = Integer.parseInt(scanner.nextLine());

                        Livro livro = livroService.encontrarLivroPorId(idLivro);
                        if (livro.getEstoque() == 0) {
                            System.out.println("Livro sem estoque");
                            return;
                        }
                        carrinhoService.inserirItemNoCarrinho(livro);
                    } catch (Exception e) {
                        System.out.println("Id inválido");
                    }

                    break;
                case "2":
                    List<Livro> livros = carrinhoService.listarItensDoCarrinho();
                    if (livros.isEmpty()) {
                        System.out.println("Carrinho vazio...");
                    }
                    for (Livro livro : livros) {
                        System.out.println("\n---");
                        System.out.println("Id: " + livro.getId());
                        System.out.println("Título: " + livro.getTitulo());
                        System.out.println("Autor: " + livro.getAutor());
                        System.out.println("N páginas: " + livro.getNumeroPaginas());
                        System.out.println("Preço: " + livro.getPreco());
                        System.out.println("---");
                    }

                    Carrinho carrinho = carrinhoService.obterCarrinho();
                    System.out.println("Valor total: " + carrinho.getValorTotal());
                    break;
                case "3":
                    try {
                        System.out.println("Insira o id do livro a ser removido");
                        int idLivro = Integer.parseInt(scanner.nextLine());

                        System.out.println("Informe a quantidade a ser removida");
                        int qtd = Integer.parseInt(scanner.nextLine());

                        carrinhoService.removerItemDoCarrinho(idLivro, qtd);
                        System.out.println("Item removido com sucesso");
                    } catch (Exception e) {
                        System.out.println("Id ou quantidade inválidos");
                    }
                    break;
                case "4":
                    System.out.println("Insira o CPF do cliente:");
                    String cpf = scanner.nextLine();

                    Cliente cliente = clienteService.encontrarClientePorCPF(cpf);
                    // cliente não existe
                    if (cliente == null) {
                        System.out.println("Cliente não cadastrado, iniciando cadastro.");

                        System.out.println("Informe o nome do cliente:");
                        String nome = scanner.nextLine().toUpperCase();

                        System.out.println("Informe o endereço do cliente:");
                        String endereco = scanner.nextLine().toUpperCase();

                        System.out.println("O cliente é um cliente vip? (Sim/Não)");
                        String resposta = scanner.nextLine().trim();

                        // Remover acentos e colocar tudo em minúsculo
                        resposta = Normalizer.normalize(resposta, Normalizer.Form.NFD)
                                .replaceAll("[^\\p{ASCII}]", "")
                                .toLowerCase();

                        boolean isVip = resposta.equals("sim");

                        Cliente novoCliente = new Cliente(nome, isVip, cpf, endereco);

                        try {
                            String json = gson.toJson(novoCliente);
                            Unirest.post("http://localhost:8080/clientes")
                                    .header("Content-Type", "application/json")
                                    .body(json)
                                    .asString();

                            cliente = novoCliente;
                        } catch (Exception e) {
                            System.out.println("Erro: " + e.getMessage());
                            return;
                        }

                    }

                    Date dataAtual = new Date();
                    SimpleDateFormat formatoData = new SimpleDateFormat("dd-MM-yyyy");
                    String dataFormatada = formatoData.format(dataAtual);

                    Double valorTotal = carrinhoService.obterCarrinho().getValorTotal();
                    List<Livro> livrosTransacao = carrinhoService.obterCarrinho().getLivros();

                    TransacaoDTO dto = new TransacaoDTO();
                    dto.transacaoTipo = TransacaoTipo.VENDA.name();
                    dto.dataInicio = dataFormatada;
                    dto.dataFim = dataFormatada;
                    dto.valor = carrinhoService.obterCarrinho().getValorTotal();
                    dto.transacaoSituacao = TransacaoSituacao.CONCLUIDA.name();
                    dto.cliente = cliente.getCPF();
                    dto.funcionario = funcionarioCadastrado.getId();
                    dto.livros = carrinhoService.obterCarrinho().getLivros().stream().map(Livro::getId).toList();

                    // transacaoService.inserirTransacao(transacao);
                    try {
                        String json = gson.toJson(dto);
                        var response = Unirest.post("http://localhost:8080/transacoes")
                                .header("Content-Type", "application/json")
                                .body(json)
                                .asString();

                        System.out.println("Venda realizada com sucesso!");
                        carrinhoService.limparCarrinho();

                        System.out.println(response.getBody());
                        System.out.println(response.getStatus());
                        System.out.println(json);

                        return;
                    } catch (Exception e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;
                case "0":
                    return;
                // break;

                default:
                    System.out.println("Opção inválida, tente novamente.");
                    break;
            }

        }

    }

    private static void realizarDevolucao() {
        while (true) {
            System.out.println("Insira o CPF do cliente:");
            String cpf = scanner.nextLine();

            if (cpf.isBlank()) {
                System.out.println("CPF obrigatório");
                return;
            }

            try {
                var response = Unirest.get("http://localhost:8080/transacoes?cpf=" + cpf)
                        .header("Content-Type", "application/json")
                        .asString();

                JSONArray transacoes = new JSONArray(response.getBody());

                if (transacoes.length() == 0) {
                    System.out.println("Nenhuma transação encontrada para o CPF informado.");
                    return;
                }

                System.out.println("\nTransações encontradas:");
                for (int i = 0; i < transacoes.length(); i++) {
                    JSONObject transacao = transacoes.getJSONObject(i);
                    System.out.println("ID: " + transacao.getInt("id"));
                    System.out.println("Tipo: " + transacao.getString("transacaoTipo"));
                    System.out.println("Início: " + transacao.getString("dataInicio"));
                    System.out.println("Fim: " + transacao.getString("dataFim"));
                    System.out.println("Valor: R$" + transacao.getDouble("valor"));
                    System.out.println("Situação: " + transacao.getString("transacaoSituacao"));
                    System.out.println("Funcionário ID: " + transacao.getInt("funcionario"));

                    JSONArray livrosArray = transacao.getJSONArray("livros");
                    List<String> titulos = new ArrayList<>();
                    for (int j = 0; j < livrosArray.length(); j++) {
                        int livroId = livrosArray.getInt(j);
                        var livroResponse = Unirest.get("http://localhost:8080/livros/" + livroId)
                                .header("Content-Type", "application/json")
                                .asString();
                        if (livroResponse.getStatus() == 200) {
                            JSONObject livro = new JSONObject(livroResponse.getBody());
                            titulos.add(livro.getString("titulo"));
                        } else {
                            titulos.add("Livro não encontrado (ID: " + livroId + ")");
                        }
                    }

                    System.out.println("Livros: " + String.join(", ", titulos));
                    System.out.println("-----------------------------------");
                }

                System.out.println("Digite o ID da transação que deseja devolver:");
                int idTransacao = Integer.parseInt(scanner.nextLine());

                JSONObject transacaoEscolhida = null;
                for (int i = 0; i < transacoes.length(); i++) {
                    JSONObject t = transacoes.getJSONObject(i);
                    if (t.getInt("id") == idTransacao) {
                        transacaoEscolhida = t;
                        break;
                    }
                }

                if (transacaoEscolhida == null) {
                    System.out.println("Transação com ID informado não encontrada.");
                    return;
                }

                JSONObject devolucaoDto = new JSONObject();
                devolucaoDto.put("transacaoTipo", "DEVOLUCAO");
                devolucaoDto.put("transacaoSituacao", "CONCLUIDA");
                devolucaoDto.put("valor", transacaoEscolhida.getDouble("valor"));
                devolucaoDto.put("dataInicio", transacaoEscolhida.getString("dataInicio"));
                devolucaoDto.put("dataFim", transacaoEscolhida.getString("dataFim"));
                devolucaoDto.put("cliente", cpf);
                devolucaoDto.put("funcionario", transacaoEscolhida.getInt("funcionario"));
                devolucaoDto.put("livros", transacaoEscolhida.getJSONArray("livros"));

                var postResponse = Unirest.post("http://localhost:8080/transacoes/devolucao")
                        .header("Content-Type", "application/json")
                        .body(devolucaoDto.toString())
                        .asString();

                System.out.println(postResponse.getBody());

            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

}