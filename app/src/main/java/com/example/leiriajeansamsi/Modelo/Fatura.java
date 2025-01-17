package com.example.leiriajeansamsi.Modelo;

import java.io.Serializable;

public class Fatura implements Serializable {
    private int id;
    private int metodoPagamentoId;
    private int metodoExpedicaoId;
    private String data;
    private float valorTotal;
    private int userdata_id;
    private StatusPedido statusPedido;

    // Enum para status do pedido
    public enum StatusPedido {
        pendente,
        pago,
        anulada;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    // Construtor vazio
    public Fatura() {
    }

    // Construtor completo
    public Fatura(int id, int metodoPagamentoId, int metodoExpedicaoId, String data, int userdata_id, float valorTotal, StatusPedido statusPedido) {
        this.id = id;
        this.metodoPagamentoId = metodoPagamentoId;
        this.metodoExpedicaoId = metodoExpedicaoId;
        this.data = data;
        this.userdata_id = userdata_id;
        this.valorTotal = valorTotal;
        this.statusPedido = statusPedido;
    }

    // Getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMetodoPagamentoId() {
        return metodoPagamentoId;
    }

    public int getUserdata_id() {
        return userdata_id;
    }

    public void setUserdata_id(int userdata_id) {
        this.userdata_id = userdata_id;
    }

    public void setMetodoPagamentoId(int metodoPagamentoId) {
        this.metodoPagamentoId = metodoPagamentoId;
    }

    public int getMetodoExpedicaoId() {
        return metodoExpedicaoId;
    }

    public void setMetodoExpedicaoId(int metodoExpedicaoId) {
        this.metodoExpedicaoId = metodoExpedicaoId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(float valorTotal) {
        this.valorTotal = valorTotal;
    }

    public StatusPedido getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(StatusPedido statusPedido) {
        this.statusPedido = statusPedido;
    }

    // Metodo auxiliar para definir status a partir de string
    public void setStatusPedidoFromString(String status) {
        try {
            this.statusPedido = StatusPedido.valueOf(status.toLowerCase()); // Converte para minúsculo antes de comparar
        } catch (IllegalArgumentException e) {
            this.statusPedido = StatusPedido.pendente;
        }
    }

    @Override
    public String toString() {
        return "Fatura{" +
                "id=" + id +
                ", metodoPagamentoId=" + metodoPagamentoId +
                ", metodoExpedicaoId=" + metodoExpedicaoId +
                ", data='" + data + '\'' +
                ", valorTotal=" + valorTotal +
                ", statusPedido=" + statusPedido +
                '}';
    }
}