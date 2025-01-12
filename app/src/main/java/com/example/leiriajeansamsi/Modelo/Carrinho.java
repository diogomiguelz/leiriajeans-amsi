package com.example.leiriajeansamsi.Modelo;

public class Carrinho {

    int id, userdataId, produto;
    float ivatotal, total;


    public Carrinho(int id, int userdataId, int produto, float ivatotal, float total) {
        this.id = id;
        this.userdataId = userdataId;
        this.produto = produto;
        this.ivatotal = ivatotal;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getuserdataId() {
        return userdataId;
    }

    public void setuserdataId(int userId) {
        this.userdataId = userId;
    }

    public float gettotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getIvatotal() {
        return total;
    }

    public void setIvatotal(float ivatotal) {
        this.ivatotal = ivatotal;
    }

    @Override
    public String toString() {
        return "Carrinho{" +
                "id=" + id +
                ", userdataId=" + userdataId +
                ", produto=" + produto + '\'' +
                ", ivatotal='" + ivatotal + '\'' +
                ", total='" + total +
                '}';
    }
}