package com.livratech.models;

import com.opencsv.bean.CsvBindByPosition;

public class Usuario {
    
    @CsvBindByPosition(position = 0)
    protected int id;

    @CsvBindByPosition(position = 1)
    protected String nome;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public Usuario(int id, String nome) {
        this.id = id;
        this.nome = nome;        
    }
}
