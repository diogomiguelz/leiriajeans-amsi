package com.example.leiriajeansamsi.Modelo;

public class LinhaCarrinho {

    private int id; // Id único para a LinhaCarrinho
    private int quantidade; // Quantidade de itens
    private float precoVenda; // Preço de venda do produto
    private float valorIva; // Valor do IVA aplicado
    private float subTotal; // Subtotal (quantidade * preçoVenda + valorIva)
    private int carrinhoId; // Id do carrinho ao qual esta linha pertence
    private Produto produto; // Alterado de produtoId para objeto Produto
    private int userdataId; // Id do usuário associado à linha

    // Construtor
    public LinhaCarrinho(int id, int quantidade, int carrinhoId, Produto produto, float valorIva, float precoVenda) {
        this.id = id;
        this.quantidade = quantidade;
        this.carrinhoId = carrinhoId;
        this.produto = produto;
        this.valorIva = valorIva;
        this.precoVenda = precoVenda;
        calcularSubTotal();
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
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

    public int getCarrinhoId() {
        return carrinhoId;
    }

    public void setCarrinhoId(int carrinhoId) {
        this.carrinhoId = carrinhoId;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getUserdataId() {
        return userdataId;
    }

    public void setUserdataId(int userdataId) {
        this.userdataId = userdataId;
    }

    // Método para calcular o subtotal com base na quantidade, preço e IVA
    public void calcularSubTotal() {
        this.subTotal = (this.quantidade * this.precoVenda) + this.valorIva;
    }
}