package com.example.leiriajeansamsi.Modelo;

public class LinhaCarrinho {
    private Produto produto;
    private int quantidade, id;

    public LinhaCarrinho(int id, int quantidade, int produtoId, Produto produto, float ivaTotal, float preco) {
        this.id = id;  // Id único para a LinhaCarrinho
        this.quantidade = quantidade;  // Quantidade de itens
        this.produto = produto;  // Produto relacionado à linha
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int id) {
        this.quantidade = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
