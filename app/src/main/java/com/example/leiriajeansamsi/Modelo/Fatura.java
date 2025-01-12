package com.example.leiriajeansamsi.Modelo;

import java.util.Date;

public class Fatura {
    int id;
    int metodopagamento;
    int metodoexpedicao;
    int userdata_id;
    String data;
    float  valorTotal;
    String statuspedido;


    public Fatura(int id, int metodopagamento, int metodoexpedicao, int userdata_id, String data, float valorTotal, String statuspedido) {
        this.id = id;
        this.metodopagamento = metodopagamento;
        this.metodoexpedicao = metodoexpedicao;
        this.userdata_id = userdata_id;
        this.data = data;
        this.valorTotal = valorTotal;
        this.statuspedido = statuspedido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMetodopagamento() {
        return metodopagamento;
    }

    public void setMetodopagamento(int metodopagamento) {
        this.metodopagamento = metodopagamento;
    }

    public int getMetodoexpedicao() {
        return metodoexpedicao;
    }

    public void setMetodoexpedicao(int metodoexpedicao) {
        this.metodoexpedicao = metodoexpedicao;
    }

    public int getUserdata_id() {
        return userdata_id;
    }

    public void setUserdata_id(int userdata_id) {
        this.userdata_id = userdata_id;
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

    public String getStatuspedido() {
        return statuspedido;
    }

    public void setStatuspedido(String statuspedido) {
        this.statuspedido = statuspedido;
    }
    // Método toString para facilitar a visualização do objeto
    @Override
    public String toString() {
        return "Fatura{" +
                "id=" + id +
                ", metodopagamento=" + metodopagamento +
                ", metodoexpedicao=" + metodoexpedicao +
                ", userdata_id=" + userdata_id +
                ", data=" + data +
                ", valorTotal=" + valorTotal +
                ", statuspedido='" + statuspedido + '\'' +
                '}';
    }
}



