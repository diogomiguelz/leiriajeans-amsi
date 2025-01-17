package com.example.leiriajeansamsi.listeners;

import com.example.leiriajeansamsi.Modelo.Carrinho;

public interface CarrinhoListener {
    void onRefreshListaCarrinho(Carrinho carrinho);
    void onCarrinhoUpdated(Carrinho carrinho);
}
