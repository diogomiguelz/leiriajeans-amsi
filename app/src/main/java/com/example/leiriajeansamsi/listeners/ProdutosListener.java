package com.example.leiriajeansamsi.listeners;

import com.example.leiriajeansamsi.Modelo.Produto;

import java.util.ArrayList;


public interface ProdutosListener {
    void onRefreshListaProdutos(ArrayList<Produto> listaProdutos);

}