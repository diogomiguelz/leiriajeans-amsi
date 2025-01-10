package com.example.leiriajeansamsi.Modelo;



public class Categoria {

    int id;
    int userdata_id;
    int produto_id;
    double ivatotal;
    double total;

    public Categoria(int id, int userdata_id, double ivatotal, int produto_id, double total) {
        this.id = id;
        this.userdata_id = userdata_id;
        this.ivatotal = ivatotal;
        this.produto_id = produto_id;
        this.total = total;
    }

    public int getUserdata_id() {
        return userdata_id;
    }

    public void setUserdata_id(int userdata_id) {
        this.userdata_id = userdata_id;
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

    public int getProduto_id() {
        return produto_id;
    }

    public void setProduto_id(int produto_id) {
        this.produto_id = produto_id;
    }

    // Método toString para facilitar a visualização do objeto
    @Override
    public String toString() {
        return "Carrinho{" +
                "id=" + id +
                ", userdata_id=" + userdata_id +
                ", produto_id=" + produto_id +
                ", ivatotal=" + ivatotal +
                ", total=" + total +
                '}';
    }
}
