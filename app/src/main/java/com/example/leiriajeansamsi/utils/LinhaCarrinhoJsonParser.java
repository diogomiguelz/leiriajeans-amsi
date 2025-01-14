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
        ArrayList<LinhaCarrinho> linhasCarrinhos = new ArrayList<>();
        try {
            SingletonProdutos singletonProdutos = SingletonProdutos.getInstance(context);

            for (int i = 0; i < response.length(); i++) {
                if (response.get(i) instanceof JSONObject) {
                    JSONObject linhaCarrinhoJSON = response.getJSONObject(i);

                    int id = linhaCarrinhoJSON.getInt("id");
                    int quantidade = linhaCarrinhoJSON.getInt("quantidade");
                    int carrinhoID = linhaCarrinhoJSON.getInt("carrinho_id");
                    int produtoID = linhaCarrinhoJSON.getInt("produto_id");
                    float precoVenda = (float) linhaCarrinhoJSON.getDouble("preco_venda");
                    float subtotal = (float) linhaCarrinhoJSON.getDouble("subtotal");
                    float valorIva = (float) linhaCarrinhoJSON.getDouble("valor_iva");

                    // Obtenha o produto correspondente ao produtoID
                    Produto produto = singletonProdutos.getProdutoById(produtoID);
                    if (produto == null) {
                        Log.e("LinhaCarrinhoJsonParser", "Produto com ID " + produtoID + " não encontrado.");
                        continue; // Pula para a próxima iteração se o produto não for encontrado
                    }

                    LinhaCarrinho linhaCarrinho = new LinhaCarrinho(id, quantidade, carrinhoID, produto, valorIva, precoVenda);
                    linhasCarrinhos.add(linhaCarrinho);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return linhasCarrinhos;
    }
}