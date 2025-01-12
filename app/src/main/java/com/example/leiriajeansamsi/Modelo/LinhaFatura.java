package com.example.leiriajeansamsi.Modelo;

public class LinhaFatura {

     int id;
     int fatura;
     int iva;
     int produto;
     float precoVenda;
     float valorIva;
     float subTotal;
     int quantidade;


    public LinhaFatura(int id, int fatura, int iva, int produto, float precoVenda, float valorIva, float subTotal, int quantidade) {
        this.id = id;
        this.fatura = fatura;
        this.iva = iva;
        this.produto = produto;
        this.precoVenda = precoVenda;
        this.valorIva = valorIva;
        this.subTotal = subTotal;
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFatura() {
        return fatura;
    }

    public void setFatura(int fatura) {
        this.fatura = fatura;
    }

    public int getIva() {
        return iva;
    }

    public void setIva(int iva) {
        this.iva = iva;
    }

    public int getProduto() {
        return produto;
    }

    public void setProduto(int produto) {
        this.produto = produto;
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

    // Método toString para facilitar a visualização do objeto
    @Override
    public String toString() {
        return "LinhaFatura{" +
                "id=" + id +
                ", fatura=" + fatura +
                ", iva=" + iva +
                ", produto=" + produto +
                ", precoVenda=" + precoVenda +
                ", valorIva=" + valorIva +
                ", subTotal=" + subTotal +
                ", quantidade=" + quantidade +
                '}';
    }
}
