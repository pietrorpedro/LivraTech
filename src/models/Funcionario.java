package models;

import enums.FuncionarioTipo;

public class Funcionario {

    private int id;
    private String nome;
    private String senha;
    private FuncionarioTipo funcionarioTipo;

    public Funcionario(String nome, String senha,FuncionarioTipo funcionarioTipo) {
        this.nome = nome;
        this.senha = senha;
        this.funcionarioTipo = funcionarioTipo;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        if (id >= 1) {
            this.id = id;
        }
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        if (nome != null && !nome.isEmpty()) {
            this.nome = nome;
        }
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        if (senha != null && !senha.isEmpty()) {
            this.senha = senha;
        }
    }
    public FuncionarioTipo getFuncionarioTipo() {
        return funcionarioTipo;
    }
    public void setFuncionarioTipo(FuncionarioTipo funcionarioTipo) {
        if (funcionarioTipo != null) {
            this.funcionarioTipo = funcionarioTipo;
        }
    }
}
