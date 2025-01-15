package com.example.leiriajeansamsi.Modelo;

import java.io.Serializable;

public class MetodoPagamento implements Serializable {
    private int id;
    private String nome;

    // Construtor vazio
    public MetodoPagamento() {
    }

    // Construtor completo
    public MetodoPagamento(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getter para id
    public int getId() {
        return id;
    }

    // Setter para id
    public void setId(int id) {
        this.id = id;
    }

    // Getter para nome
    public String getNome() {
        return nome;
    }

    // Setter para nome
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "MetodoPagamento{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
