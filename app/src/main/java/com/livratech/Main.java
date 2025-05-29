package com.livratech;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

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

public class Main {

    private static Funcionario funcionarioCadastrado;
    private static final Scanner scanner = new Scanner(System.in);
    private static final ClienteService clienteService = new ClienteService();

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

                scanner.close();
                break;
            } else {
                System.out.println("Dados incorretos, tente novamente. \n");
            }
        }
    }

    public static void opcoes() {

        while (true) {
            System.out.println("--------------------");
            System.out.println("Selecione uma das opções");
            System.out.println("--------------------");

            System.out.println("(1) Realizar venda");
            System.out.println("(2) Realizar devolução");
            System.out.println("(3) Realizar troca");
            System.out.println("(0) Sair");

            String escolha = scanner.nextLine();

            switch (escolha) {
                case "1":

                    break;
                case "2":

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
                    
                    break;
                case "2":
                    
                    break;
                case "3":
                    
                    break;
                case "4":
                    
                    break;
                case "0":
                    
                    break;
            
                default:
                    System.out.println("Opção inválida, tente novamente.");
                    break;
            }


            // System.out.println("Insira o CPF do cliente:");
            // String cpf = scanner.nextLine();

            // Cliente cliente = clienteService.encontrarClientePorCPF(cpf);
            // // cliente não existe
            // if (cliente == null) {
            //     System.out.println("Cliente não cadastrado, iniciando cadastro.");

            //     System.out.println("Informe o nome do cliente:");
            //     String nome = scanner.nextLine().toUpperCase();

            //     System.out.println("Informe o endereço do cliente:");
            //     String endereco = scanner.nextLine().toUpperCase();

            //     System.out.println("O cliente é um cliente vip? (Sim/Não)");
            //     String resposta = scanner.nextLine().trim();

            //     // Remover acentos e colocar tudo em minúsculo
            //     resposta = Normalizer.normalize(resposta, Normalizer.Form.NFD)
            //             .replaceAll("[^\\p{ASCII}]", "")
            //             .toLowerCase();

            //     boolean isVip = resposta.equals("sim");

            //     Cliente novoCliente = new Cliente(nome, isVip, cpf, endereco);
            //     clienteService.inserirCliente(novoCliente);
            //     cliente = novoCliente;
            // }


        }

    }
}