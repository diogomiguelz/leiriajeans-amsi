package com.example.leiriajeansamsi.utils;

import android.content.Context;
import android.util.Log;

import com.example.leiriajeansamsi.Modelo.LinhaCarrinho;
import com.example.leiriajeansamsi.Modelo.Produto;
import com.example.leiriajeansamsi.Modelo.SingletonProdutos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LinhaCarrinhoJsonParser {
    private static final String TAG = "LinhaCarrinhoJsonParser";

    public static LinhaCarrinho parseLinhaCarrinho(JSONObject jsonLinhaCarrinho, Context context) throws JSONException {
        int id = jsonLinhaCarrinho.getInt("id");
        int quantidade = jsonLinhaCarrinho.getInt("quantidade");
        int carrinhoId = jsonLinhaCarrinho.getInt("carrinho_id");
        int produtoId = jsonLinhaCarrinho.getInt("produto_id");
        float precoVenda = (float) jsonLinhaCarrinho.getDouble("precoVenda");
        float valorIva = (float) jsonLinhaCarrinho.getDouble("valorIva");

        Produto produto = SingletonProdutos.getInstance(context).getProdutoById(produtoId);
        if (produto == null) {
            throw new JSONException("Produto não encontrado: " + produtoId);
        }

        return new LinhaCarrinho(id, quantidade, carrinhoId, produto, valorIva, precoVenda);
    }

    public static ArrayList<LinhaCarrinho> parserJsonLinhaCarrinho(JSONArray response, Context context) {
        ArrayList<LinhaCarrinho> linhasCarrinho = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject linhaJson = response.getJSONObject(i);
                LinhaCarrinho linha = parseLinhaCarrinho(linhaJson, context);
                linhasCarrinho.add(linha);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Erro ao fazer parse das linhas do carrinho: " + e.getMessage());
        }
        return linhasCarrinho;
    }

    public static LinhaCarrinho criarLinhaCarrinho(JSONObject response, Context context) throws JSONException {
        // Verifica se a resposta é válida
        if (response == null) {
            throw new JSONException("Resposta nula");
        }

        // Tenta fazer o parse diretamente da resposta
        return parseLinhaCarrinho(response, context);
    }


    public static LinhaCarrinho atualizarLinhaCarrinho(JSONObject response, Context context) throws JSONException {
        return criarLinhaCarrinho(response, context);
    }
}