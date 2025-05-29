package com.livratech.models;

import com.livratech.enums.FuncionarioTipo;
import com.livratech.utils.CSVIDGenerator;
import com.opencsv.bean.CsvBindByPosition;

public class Funcionario extends Usuario{

    @CsvBindByPosition(position = 2)
    private String senha;

    @CsvBindByPosition(position = 3)
    private FuncionarioTipo funcionarioTipo;

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public FuncionarioTipo getFuncionarioTipo() {
        return funcionarioTipo;
    }

    public void setFuncionarioTipo(FuncionarioTipo funcionarioTipo) {
        this.funcionarioTipo = funcionarioTipo;
    }

    public Funcionario() {
        super(0, "");
    }

    public Funcionario(
        String nome,
        String senha,
        FuncionarioTipo funcionarioTipo
        ) {     
        super(CSVIDGenerator.generate("app\\src\\main\\java\\com\\livratech\\data\\funcionarios.csv"), nome);
        this.senha = senha;
        this.funcionarioTipo = funcionarioTipo;
    }
    
}
