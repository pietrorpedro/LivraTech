package com.livratech.models;

import com.livratech.utils.CSVIDGenerator;
import com.opencsv.bean.CsvBindByPosition;

public class Cliente extends Usuario{

    @CsvBindByPosition(position = 2)
    private boolean clienteVip;

    @CsvBindByPosition(position = 3)
    private String CPF;

    @CsvBindByPosition(position = 4)
    private String endereco;

    public boolean isClienteVip() {
        return clienteVip;
    }

    public void setClienteVip(boolean clienteVip) {
        this.clienteVip = clienteVip;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String cPF) {
        CPF = cPF;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Cliente() {
        super(0, "");
    }

    public Cliente( 
        String nome,
        boolean clienteVip,
        String CPF,
        String endereco
        ) {
        super(CSVIDGenerator.generate("app\\src\\main\\java\\com\\livratech\\data\\clientes.csv"), nome);
        this.clienteVip = clienteVip;
        this.CPF = CPF;
        this.endereco = endereco;
    }
    
}
