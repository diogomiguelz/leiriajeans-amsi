package com.example.leiriajeansamsi.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.example.leiriajeansamsi.Modelo.LinhaCarrinho;

public class LinhaCarrinhoJsonParser {

    public static ArrayList<LinhaCarrinho> parserJsonLinhaCarrinho(JSONArray response) {
        ArrayList<LinhaCarrinho> linhasCarrinhos = new ArrayList<LinhaCarrinho>();
        try {
            for (int i = 0; i < response.length(); i++) {
                if (response.get(i) instanceof JSONObject) {
                    JSONObject linhaCarrinhoJSON = response.getJSONObject(i);

                    // Extraindo os dados do JSON
                    int id = linhaCarrinhoJSON.getInt("id");
                    int quantidade = linhaCarrinhoJSON.getInt("quantidade");
                    int carrinhoID = linhaCarrinhoJSON.getInt("carrinho_id");
                    int produtoID = linhaCarrinhoJSON.getInt("produto_id");
                    float precoVenda = (float) linhaCarrinhoJSON.getDouble("preco_venda");
                    float subtotal = (float) linhaCarrinhoJSON.getDouble("subtotal");
                    float valorIva = (float) linhaCarrinhoJSON.getDouble("valor_iva");

                    // Criar o objeto LinhaCarrinho com os dados extraídos
                    LinhaCarrinho linhaCarrinho = new LinhaCarrinho(id, quantidade, carrinhoID, produtoID, valorIva, precoVenda, subtotal);

                    // Adicionar à lista
                    linhasCarrinhos.add(linhaCarrinho);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return linhasCarrinhos;
    }
}