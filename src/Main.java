import com.sun.net.httpserver.HttpServer;
import enums.FuncionarioTipo;
import enums.TransacaoSituacao;
import enums.TransacaoTipo;
import models.Cliente;
import models.Funcionario;
import models.Livro;
import models.Transacao;
import services.Clientes;
import services.Estoque;
import services.Funcionarios;
import services.Transacoes;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

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
        List<Livro> carrinho = new ArrayList<>();
        Estoque estoque = new Estoque();
        Clientes clientes = new Clientes();

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
                    System.out.print("Digite o ID do livro: ");
                    int idLivro = sc.nextInt();
                    sc.nextLine();

                    Livro livro = estoque.buscarLivroPorId(idLivro);
                    if (livro != null) {
                        carrinho.add(livro);
                        System.out.println("Livro adicionado: " + livro.getTitulo());
                    } else {
                        System.out.println("Livro não encontrado.");
                    }
                    break;

                case 2:
                    System.out.println("Informe o CPF do Cliente:");
                    String cpf = sc.nextLine();

                    Cliente cliente = clientes.buscarClientePorCpf(cpf);

                    if (cliente == null) {
                        System.out.println("Cliente não encontrado. Preencha o formulário para cadastro:");
                        System.out.println("Nome:");
                        String nome = sc.nextLine();
                        System.out.println("Endereço:");
                        String endereco = sc.nextLine();

                        cliente = new Cliente(nome, cpf, endereco, false);
                        clientes.adicionarCliente(cliente);
                        System.out.println("Cliente cadastrado com sucesso.");
                    }

                    double valorTotal = carrinho.stream().mapToDouble(Livro::getPreco).sum();
                    Transacao transacao = new Transacao(
                            new Date(),
                            new Date(),
                            valorTotal,
                            TransacaoSituacao.CONCLUIDA,
                            TransacaoTipo.VENDA,
                            cliente.getCPF(),
                            funcionarioLogado.getNome(),
                            carrinho
                    );

                    Transacoes transacoes = new Transacoes();
                    transacoes.adicionarTransacao(transacao);
                    carrinho.clear();
                    System.out.println("Venda realizada com sucesso.");

                    return;

                case 0:
                    System.out.println("Venda cancelada.");
                    return;

                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        }
    }

    private static void iniciarApi() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.start();
    }

    public static void chamarApi(String rota, String metodo, String jsonData) throws Exception {
        URL url = new URL("http://localhost:8080" + rota);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(metodo);
        conn.setDoOutput(true);

        if (!jsonData.isEmpty()) {
            byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(input, 0, input.length);
        }
    }
}
