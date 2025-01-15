package com.example.leiriajeansamsi.Modelo;

public class LinhaFatura {
    private int id;
    private int faturaId;
    private int ivaId;
    private int produtoId;
    private float precoVenda;
    private float valorIva;
    private float subTotal;
    private int quantidade;

    // Construtor vazio
    public LinhaFatura() {
    }

    // Construtor completo
    public LinhaFatura(int id, int faturaId, int ivaId, int produtoId, float precoVenda, float valorIva, float subTotal, int quantidade) {
        this.id = id;
        this.faturaId = faturaId;
        this.ivaId = ivaId;
        this.produtoId = produtoId;
        this.precoVenda = precoVenda;
        this.valorIva = valorIva;
        this.subTotal = subTotal;
        this.quantidade = quantidade;
    }

    // Getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFaturaId() {
        return faturaId;
    }

    public void setFaturaId(int faturaId) {
        this.faturaId = faturaId;
    }

    public int getIvaId() {
        return ivaId;
    }

    public void setIvaId(int ivaId) {
        this.ivaId = ivaId;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public float getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(float precoVenda) {
        this.precoVenda = precoVenda;
    }

    public float getValorIva() {
        return valorIva;
    }

    public void setValorIva(float valorIva) {
        this.valorIva = valorIva;
    }

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "LinhaFatura{" +
                "id=" + id +
                ", faturaId=" + faturaId +
                ", ivaId=" + ivaId +
                ", produtoId=" + produtoId +
                ", precoVenda=" + precoVenda +
                ", valorIva=" + valorIva +
                ", subTotal=" + subTotal +
                ", quantidade=" + quantidade +
                '}';
    }
}
