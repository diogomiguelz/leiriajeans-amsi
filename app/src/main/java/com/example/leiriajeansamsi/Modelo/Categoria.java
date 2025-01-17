package com.example.leiriajeansamsi.Modelo;

public class Categoria {

    private int id;
    private int userdata;
    private int produto;
    private double ivatotal;
    private double total;

    public Categoria(int id, int userdata, double ivatotal, int produto, double total) {
        this.id = id;
        this.userdata = userdata;
        this.ivatotal = ivatotal;
        this.produto = produto;
        this.total = total;
    }

    public int getUserdata_id() {
        return userdata;
    }

    public void setUserdata_id(int userdata_id) {
        this.userdata = userdata_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getIvatotal() {
        return ivatotal;
    }

    public void setIvatotal(double ivatotal) {
        this.ivatotal = ivatotal;
    }

    public int getProduto() {
        return produto;
    }

    public void setProduto(int produto) {
        this.produto = produto;
    }

    // Metodo toString para facilitar a visualização do objeto
    @Override
    public String toString() {
        return "Carrinho{" +
                "id=" + id +
                ", userdata=" + userdata +
                ", produto=" + produto +
                ", ivatotal=" + ivatotal +
                ", total=" + total +
                '}';
    }
}
