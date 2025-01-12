package com.example.leiriajeansamsi.Modelo;

public class Produto {

    int id;
    String nome;
    String descricao;
    double preco;
    String sexo;
    int stock;
    int cor_id;
    int iva_id;
    int categoria_id;

    public Produto(int id, String nome, String descricao, double preco, String sexo, int stock, int cor_id, int iva_id, int categoria_id) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.sexo = sexo;
        this.stock = stock;
        this.cor_id = cor_id;
        this.iva_id = iva_id;
        this.categoria_id = categoria_id;
    }

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

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getCor_id() {
        return cor_id;
    }

    public void setCor_id(int cor_id) {
        this.cor_id = cor_id;
    }

    public int getIva_id() {
        return iva_id;
    }

    public void setIva_id(int iva_id) {
        this.iva_id = iva_id;
    }

    public int getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(int categoria_id) {
        this.categoria_id = categoria_id;
    }

    // Método toString para facilitar a visualização do objeto
    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco=" + preco +
                ", sexo='" + sexo + '\'' +
                ", stock=" + stock +
                ", cor_id=" + cor_id +
                ", iva_id=" + iva_id +
                ", categoria_id=" + categoria_id +
                '}';
    }
}
