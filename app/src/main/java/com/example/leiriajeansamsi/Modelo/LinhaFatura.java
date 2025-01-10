package com.example.leiriajeansamsi.Modelo;

public class LinhaFatura {

     int id;
     int fatura_id;
     int iva_id;
     int produto_id;
     float precoVenda;
     float valorIva;
     float subTotal;
     int quantidade;


    public LinhaFatura(int id, int fatura_id, int iva_id, int produto_id, float precoVenda, float valorIva, float subTotal, int quantidade) {
        this.id = id;
        this.fatura_id = fatura_id;
        this.iva_id = iva_id;
        this.produto_id = produto_id;
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

    public int getFatura_id() {
        return fatura_id;
    }

    public void setFatura_id(int fatura_id) {
        this.fatura_id = fatura_id;
    }

    public int getIva_id() {
        return iva_id;
    }

    public void setIva_id(int iva_id) {
        this.iva_id = iva_id;
    }

    public int getProduto_id() {
        return produto_id;
    }

    public void setProduto_id(int produto_id) {
        this.produto_id = produto_id;
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
                ", fatura_id=" + fatura_id +
                ", iva_id=" + iva_id +
                ", produto_id=" + produto_id +
                ", precoVenda=" + precoVenda +
                ", valorIva=" + valorIva +
                ", subTotal=" + subTotal +
                ", quantidade=" + quantidade +
                '}';
    }
}
