package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpServer;
import enums.FuncionarioTipo;
import enums.TransacaoSituacao;
import enums.TransacaoTipo;
import models.Funcionario;
import org.example.api.CarrinhoHandler;
import org.example.api.ClienteHandler;
import org.example.api.TransacaoHandler;
import org.example.models.Cliente;
import org.example.models.Livro;
import org.example.models.LivroCarrinho;
import org.example.models.Transacao;
import org.example.services.CarrinhoDeCompras;
import org.example.services.Estoque;
import services.Funcionarios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.*;

class Main {

    private static Funcionario funcionarioLogado;

    public static void main(String[] args) throws IOException {
        iniciarApi();

        System.out.println("------------------------------------");
        System.out.println("Bem-vindo(a) ao sistema da LivraTech");
        System.out.println("------------------------------------");

        while (true) {
            while (!login()) {
                System.out.println("Credenciais inválidas. Tente novamente\n");
            }

            System.out.println("Login realizado com sucesso!");
            acoes();
        }

//        String response = fazerRequisicaoHttp("GET", "/carrinho/obter", null);
//        System.out.println(response);

//        Gson gson = new Gson();
//        String responseCarrinho = fazerRequisicaoHttp("GET", "/carrinho/obter", null);
//        Type tipo = new TypeToken<List<LivroCarrinho>>(){}.getType();
//        List<LivroCarrinho> livros = gson.fromJson(responseCarrinho, tipo);
//
//        List<Livro> livrosLista = new ArrayList<>();
//        for (LivroCarrinho lc : livros) {
//            int qtd = (int) lc.getQuantidade();
//            for (int i = 0; i < qtd; i++) {
//                Livro l = new Livro(
//                        lc.getLivro().getTitulo(),
//                        lc.getLivro().getAutor(),
//                        lc.getLivro().getPreco(),
//                        lc.getLivro().getnPaginas(),
//                        lc.getLivro().getCategoriaLivro()
//                );
//                livrosLista.add(l);
//            }
//        }
//
//
//
//        for (Livro livro : livrosLista) {
//            System.out.println(livro.getTitulo());
//        }





    }

    public static boolean login() {
        Scanner sc = new Scanner(System.in);
        Funcionarios funcionarios = new Funcionarios();

        System.out.println("Nome do Funcionário:");
        String nome = sc.nextLine();

        System.out.println("Senha:");
        String senha = sc.nextLine();

        Funcionario autenticado = funcionarios.autenticar(nome, senha);
        funcionarioLogado = autenticado;

        return autenticado != null;
    }

    public static void acoes() {
        Scanner sc = new Scanner(System.in);

        boolean sair = false;
        while (!sair) {
            System.out.println("------------------------------------");
            System.out.println(funcionarioLogado.getNome() + ", Selecione uma ação");
            System.out.println("Tipo de Funcionário: " + funcionarioLogado.getFuncionarioTipo());
            System.out.println("------------------------------------");

            System.out.println("(1) Registrar venda");
            System.out.println("(2) Registrar devolução");
            if (funcionarioLogado.getFuncionarioTipo().equals(FuncionarioTipo.ADMINISTRADOR)) {
                System.out.println("(3) Cadastrar Livro");
            }
            System.out.println("(0) Sair");

            int escolha = sc.nextInt();

            switch (escolha) {
                case 1:
                    registrarVenda();
                    break;
                case 2:
                    System.out.println("Devolução");
                    break;
                case 3:
                    if (funcionarioLogado.getFuncionarioTipo().equals(FuncionarioTipo.ADMINISTRADOR)) {
                        System.out.println("Cadastrar Livro");
                    } else {
                        System.out.println("Você não tem permissão para isso.");
                    }
                    break;
                case 0:
                    System.out.println("Saindo.");
                    sair = true;
                    break;
                default:
                    System.out.println("Opção inválida, tente novamente.");
                    break;
            }
        }
    }

    public static void registrarVenda() {
        Scanner sc = new Scanner(System.in);
        Gson gson = new Gson();

        while (true) {
            System.out.println("----------------");
            System.out.println("Registrar venda");
            System.out.println("(1) Adicionar livro ao carrinho");
            System.out.println("(2) Finalizar venda");
            System.out.println("(0) Cancelar");
            System.out.println("----------------");

            int opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    boolean continuarAdicionando = true;
                    while (continuarAdicionando) {
                        System.out.print("ID do livro: ");
                        int idLivro = sc.nextInt();
                        System.out.print("Quantidade: ");
                        int quantidade = sc.nextInt();
                        sc.nextLine(); // Limpa o buffer após as leituras de números

                        Map<String, Object> corpo = new HashMap<>();
                        corpo.put("idLivro", idLivro);
                        corpo.put("quantidade", quantidade);

                        String json = gson.toJson(corpo);

                        String respostaAdicionar = fazerRequisicaoHttp("POST", "/carrinho/adicionar", json);
                        if (respostaAdicionar != null) {
                            break;
                        } else {
                            System.out.println("Erro ao adicionar livro ao carrinho.");
                            continuarAdicionando = false;
                        }
                    }
                    break;

                case 2:
                    String response = fazerRequisicaoHttp("GET", "/carrinho/obter", null);
                    if (response.equals("[]")) {
                        System.out.println("Carrinho vazio, impossível realizar venda.");
                        return;
                    }


                    System.out.println("CPF do cliente:");
                    String cpf = sc.nextLine();

                    String clienteResponse = fazerRequisicaoHttp("GET", "/cliente/buscar?cpf=" + cpf, null);
                    if (clienteResponse == null) {
                        System.out.println("Cliente não cadastrado no sistema. Iniciar Cadastro:");

                        System.out.println("Nome completo do cliente:");
                        String nomeCompleto = sc.nextLine();

                        System.out.println("Endereço do cliente:");
                        String endereco = sc.nextLine();

                        Cliente cliente = new Cliente(nomeCompleto, cpf, endereco, false);

                        String jsonCliente = gson.toJson(cliente);

                        String cadastroResponse = fazerRequisicaoHttp("POST", "/cliente/adicionar", jsonCliente);

                        if (cadastroResponse != null && cadastroResponse.equals("true")) {
                            System.out.println("Cliente cadastrado com sucesso!");
                        } else {
                            System.out.println("Erro ao cadastrar cliente. Tente novamente.");
                            return;
                        }
                    }


                    String responseCarrinho = fazerRequisicaoHttp("GET", "/carrinho/obter", null);
                    Object as = gson.fromJson(response, List.class).get(0);

                    Type tipo = new TypeToken<List<LivroCarrinho>>(){}.getType();
                    List<LivroCarrinho> livros = gson.fromJson(response, tipo);

                    Double valorTotal = 0.0;
                    for (LivroCarrinho livroCarrinho : livros) {
                        valorTotal += livroCarrinho.getValorTotal();
                    }

                    String responseCarrinhoLivros = fazerRequisicaoHttp("GET", "/carrinho/obter", null);
                    Type tipos = new TypeToken<List<LivroCarrinho>>(){}.getType();
                    List<LivroCarrinho> livross = gson.fromJson(responseCarrinho, tipo);

                    List<Livro> livrosLista = new ArrayList<>();
                    for (LivroCarrinho lc : livross) {
                        int qtd = (int) lc.getQuantidade();
                        for (int i = 0; i < qtd; i++) {
                            Livro l = new Livro(
                                    lc.getLivro().getTitulo(),
                                    lc.getLivro().getAutor(),
                                    lc.getLivro().getPreco(),
                                    lc.getLivro().getnPaginas(),
                                    lc.getLivro().getCategoriaLivro()
                            );
                            l.setId(lc.getLivro().getId());
                            livrosLista.add(l);
                        }
                    }

                    Transacao transacao = new Transacao(
                            new Date(),
                            new Date(),
                            valorTotal,
                            TransacaoSituacao.CONCLUIDA,
                            TransacaoTipo.VENDA,
                            cpf,
                            funcionarioLogado.getNome(),
                            livrosLista
                    );

                    // REQUISIÇÃO AQUI!
                    String jsonTransacao = gson.toJson(transacao);
                    String resposta = fazerRequisicaoHttp("POST", "/transacao/criar", jsonTransacao);

                    if (resposta != null && resposta.contains("sucesso")) {
                        System.out.println("Venda registrada com sucesso!");
                        fazerRequisicaoHttp("POST", "/carrinho/limpar", null);
                    } else {
                        System.out.println("Erro ao registrar a venda.");
                    }

                    break;

                case 0:
                    System.out.println("Venda cancelada.");
                    break;

                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        }
    }

    private static void iniciarApi() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // carrinho
        server.createContext("/carrinho/adicionar", new CarrinhoHandler());
        server.createContext("/carrinho/obter", new CarrinhoHandler());
        server.createContext("/carrinho/total", new CarrinhoHandler());
        server.createContext("/carrinho/limpar", new CarrinhoHandler());

        // livros

        // cliente
        server.createContext("/cliente/adicionar", new ClienteHandler());
        server.createContext("/cliente/buscar", new ClienteHandler());

        // transação
        server.createContext("/transacao/criar", new TransacaoHandler());

        server.setExecutor(null);
        server.start();
    }

    public static String fazerRequisicaoHttp(String metodo, String caminho, String corpoJson) {
        try {
            URL url = new URL("http://localhost:8080" + caminho);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(metodo.toUpperCase());
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            if (corpoJson != null && !corpoJson.isEmpty()) {
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(corpoJson.getBytes());
                    os.flush();
                }
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                StringBuilder resposta = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()))) {
                    String linha;
                    while ((linha = reader.readLine()) != null) {
                        resposta.append(linha);
                    }
                }
                return resposta.toString();
            }

            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
