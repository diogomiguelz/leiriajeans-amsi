package com.example.leiriajeansamsi.Modelo;

import java.io.Serializable;

public class MetodoExpedicao implements Serializable {
    private int id;
    private String nome;
    private String descricao;
    private float custo;
    private int prazoEntrega;

    // Construtor vazio
    public MetodoExpedicao() {
    }

    // Construtor completo
    public MetodoExpedicao(int id, String nome, String descricao, float custo, int prazoEntrega) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.custo = custo;
        this.prazoEntrega = prazoEntrega;
    }

    // Getters e setters
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public float getCusto() {
        return custo;
    }

    public void setCusto(float custo) {
        this.custo = custo;
    }

    public int getPrazoEntrega() {
        return prazoEntrega;
    }

    public void setPrazoEntrega(int prazoEntrega) {
        this.prazoEntrega = prazoEntrega;
    }

    @Override
    public String toString() {
        return "MetodoExpedicao{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", custo=" + custo +
                ", prazoEntrega=" + prazoEntrega +
                '}';
    }
}
