package com.example.leiriajeansamsi.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.example.leiriajeansamsi.Modelo.LinhaCarrinho;
import com.example.leiriajeansamsi.Modelo.Produto;
import com.example.leiriajeansamsi.Modelo.SingletonProdutos;

public class LinhaCarrinhoJsonParser {

    public static ArrayList<LinhaCarrinho> parserJsonLinhaCarrinho(JSONArray response, Context context) {
        ArrayList<LinhaCarrinho> linhasCarrinho = new ArrayList<>();
        try {
            SingletonProdutos singletonProdutos = SingletonProdutos.getInstance(context);

            for (int i = 0; i < response.length(); i++) {
                JSONObject linhaJson = response.getJSONObject(i);
                
                int id = linhaJson.getInt("id");
                int quantidade = linhaJson.getInt("quantidade");
                int carrinhoId = linhaJson.getInt("carrinho_id");
                int produtoId = linhaJson.getInt("produto_id");
                float precoVenda = (float) linhaJson.getDouble("precoVenda");
                float valorIva = (float) linhaJson.getDouble("valorIva");

                // Obter o produto correspondente ao produtoId
                Produto produto = singletonProdutos.getProdutoById(produtoId);
                if (produto == null) {
                    Log.e("LinhaCarrinhoJsonParser", "Produto com ID " + produtoId + " não encontrado.");
                    continue;
                }

                // Usar o construtor correto
                LinhaCarrinho linhaCarrinho = new LinhaCarrinho(id, quantidade, carrinhoId, produto, valorIva, precoVenda);
                linhasCarrinho.add(linhaCarrinho);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return linhasCarrinho;
    }
}