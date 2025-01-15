package com.example.leiriajeansamsi.utils;

import com.example.leiriajeansamsi.Modelo.LinhaFatura;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LinhasFaturasJsonParser {

    // Metodo para parsear uma única linha de fatura
    public static LinhaFatura parseLinhaFatura(JSONObject jsonLinhaFatura) throws JSONException {
        LinhaFatura linha = new LinhaFatura();
        linha.setId(jsonLinhaFatura.getInt("id"));
        linha.setFaturaId(jsonLinhaFatura.optInt("fatura_id"));
        linha.setProdutoId(jsonLinhaFatura.optInt("produto_id"));
        linha.setQuantidade(jsonLinhaFatura.optInt("quantidade"));
        linha.setPrecoVenda((float) jsonLinhaFatura.optDouble("precoVenda"));
        linha.setValorIva((float) jsonLinhaFatura.optDouble("valorIva"));
        linha.setSubTotal((float) jsonLinhaFatura.optDouble("subTotal"));
        return linha;
    }

    // Método para parsear múltiplas linhas de faturas a partir de um JSONArray
    public static ArrayList<LinhaFatura> parserJsonLinhasFaturas(JSONArray response) {
        ArrayList<LinhaFatura> linhasFaturas = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject linhaJson = response.getJSONObject(i);
                LinhaFatura linha = new LinhaFatura(
                        linhaJson.getInt("id"),
                        linhaJson.getInt("fatura_id"),
                        linhaJson.optInt("iva_id", 0),
                        linhaJson.getInt("produto_id"),
                        (float) linhaJson.getDouble("precoVenda"),
                        (float) linhaJson.getDouble("valorIva"),
                        (float) linhaJson.getDouble("subTotal"),
                        linhaJson.getInt("quantidade")
                );
                linhasFaturas.add(linha);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return linhasFaturas;
    }
}
