package com.example.leiriajeansamsi.Modelo;

import java.util.ArrayList;
import java.util.List;

public class Carrinho {
    private int id;
    private int userdataId;
    private int produtoId;
    private float ivatotal;
    private float total;
    private List<LinhaCarrinho> linhas;

    // Construtor padrão inicializa a lista de linhas
    public Carrinho() {
        this.linhas = new ArrayList<>();
    }

    // Construtor completo
    public Carrinho(int id, int userdataId, int produtoId, float ivatotal, float total) {
        this.id = id;
        this.userdataId = userdataId;
        this.produtoId = produtoId;
        this.ivatotal = ivatotal;
        this.total = total;
    }


    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserdataId() {
        return userdataId;
    }

    public void setUserdataId(int userdataId) {
        this.userdataId = userdataId;
    }

    public float getIvatotal() {
        return ivatotal;
    }

    public void setIvatotal(float ivatotal) {
        this.ivatotal = ivatotal;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public List<LinhaCarrinho> getLinhas() {
        return linhas;
    }

    // Adicionar uma linha ao carrinho
    public void adicionarLinha(LinhaCarrinho linha) {
        this.linhas.add(linha);
    }

    // Remover uma linha do carrinho
    public void removerLinha(LinhaCarrinho linha) {
        this.linhas.remove(linha);
    }

    @Override
    public String toString() {
        return "Carrinho{" +
                "id=" + id +
                ", userdataId=" + userdataId +
                ", produtoId=" + produtoId +
                ", ivatotal=" + ivatotal +
                ", total=" + total +
                ", linhas=" + linhas +
                '}';
    }
}
