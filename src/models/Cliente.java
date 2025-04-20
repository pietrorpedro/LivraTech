package models;

public class Cliente {

    private int id;
    private String nome;
    private String CPF;
    private String endereco;
    private boolean clienteVip;

    public Cliente(
            String nome,
            String CPF,
            String endereco,
            boolean clienteVip
    ) {
        this.nome = nome;
        this.CPF = CPF;
        this.endereco = endereco;
        this.clienteVip = clienteVip;
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
    public String getCPF() {
        return CPF;
    }
    public void setCPF(String CPF) {
        if (CPF != null && !CPF.isEmpty()) {
            this.CPF = CPF;
        }
    }
    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        if (endereco != null && !endereco.isEmpty()) {
            this.endereco = endereco;
        }
    }
    public boolean isClienteVip() {
        return clienteVip;
    }
    public void setClienteVip(boolean clienteVip) {
        this.clienteVip = clienteVip;
    }
}
