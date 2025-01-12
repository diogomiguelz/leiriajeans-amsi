package com.example.leiriajeansamsi.Modelo;

public class Carrinho {

    int id, userId;
    String metodoEnvio, status, dtaPedido;
    float valorTotal;


    public Carrinho(int id, int userId, String metodoEnvio, String status, String dtaPedido, float valorTotal) {
        this.id = id;
        this.userId = userId;
        this.metodoEnvio = metodoEnvio;
        this.status = status;
        this.dtaPedido = dtaPedido;
        this.valorTotal = valorTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMetodoEnvio() {
        return metodoEnvio;
    }

    public void setMetodoEnvio(String metodoEnvio) {
        this.metodoEnvio = metodoEnvio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDtaPedido() {
        return dtaPedido;
    }

    public void setDtaPedido(String dtaPedido) {
        this.dtaPedido = dtaPedido;
    }

    public float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(float valorTotal) {
        this.valorTotal = valorTotal;
    }

    @Override
    public String toString() {
        return "Carrinho{" +
                "id=" + id +
                ", userId=" + userId +
                ", metodoEnvio='" + metodoEnvio + '\'' +
                ", status='" + status + '\'' +
                ", dtaPedido='" + dtaPedido + '\'' +
                ", valorTotal=" + valorTotal +
                '}';
    }
}
