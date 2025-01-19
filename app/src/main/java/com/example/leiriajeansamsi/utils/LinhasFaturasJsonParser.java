package com.example.leiriajeansamsi.utils;

import android.util.Log;

import com.example.leiriajeansamsi.Modelo.LinhaFatura;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LinhasFaturasJsonParser {
    private static final String TAG = "LinhasFaturasJsonParser";

    public static LinhaFatura parseLinhaFatura(JSONObject jsonLinhaFatura, int faturaId) throws JSONException {
        LinhaFatura linha = new LinhaFatura();
        linha.setId(jsonLinhaFatura.optInt("id"));
        linha.setFaturaId(faturaId);
        linha.setProdutoId(jsonLinhaFatura.optInt("produto_id"));
        linha.setQuantidade(jsonLinhaFatura.optInt("quantidade"));

        linha.setPrecoVenda((float) jsonLinhaFatura.optDouble("precoVenda", 0.0));
        linha.setValorIva((float) jsonLinhaFatura.optDouble("valorIva", 0.0));
        linha.setSubTotal((float) jsonLinhaFatura.optDouble("subTotal", 0.0));

        Log.d(TAG, "Linha parseada: " + linha.toString());
        return linha;
    }


    public static ArrayList<LinhaFatura> parserJsonLinhasFaturas(JSONArray response, int faturaId) {
        ArrayList<LinhaFatura> linhasFaturas = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject linhaJson = response.getJSONObject(i);
                LinhaFatura linha = parseLinhaFatura(linhaJson, faturaId);
                linhasFaturas.add(linha);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Erro ao fazer parse das linhas da fatura: " + e.getMessage());
        }
        return linhasFaturas;
    }

    public static LinhaFatura criarLinhaFatura(JSONObject response) throws JSONException {
        return parseLinhaFatura(response, 0);
    }
}
