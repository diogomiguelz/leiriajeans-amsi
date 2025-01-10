package com.example.leiriajeansamsi.Modelo;

import java.util.Date;

public class Fatura {
    int id;
    int metodopagamento_id;
    int metodoexpedicao_id;
    int userdata_id;
    Date data;
    float  valorTotal;
    String statuspedido;


    public Fatura(int id, int metodopagamento_id, int metodoexpedicao_id, int userdata_id, Date data, float valorTotal, String statuspedido) {
        this.id = id;
        this.metodopagamento_id = metodopagamento_id;
        this.metodoexpedicao_id = metodoexpedicao_id;
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

    public int getMetodopagamento_id() {
        return metodopagamento_id;
    }

    public void setMetodopagamento_id(int metodopagamento_id) {
        this.metodopagamento_id = metodopagamento_id;
    }

    public int getMetodoexpedicao_id() {
        return metodoexpedicao_id;
    }

    public void setMetodoexpedicao_id(int metodoexpedicao_id) {
        this.metodoexpedicao_id = metodoexpedicao_id;
    }

    public int getUserdata_id() {
        return userdata_id;
    }

    public void setUserdata_id(int userdata_id) {
        this.userdata_id = userdata_id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
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
                ", metodopagamento_id=" + metodopagamento_id +
                ", metodoexpedicao_id=" + metodoexpedicao_id +
                ", userdata_id=" + userdata_id +
                ", data=" + data +
                ", valorTotal=" + valorTotal +
                ", statuspedido='" + statuspedido + '\'' +
                '}';
    }
}



