package com.example.leiriajeansamsi.utils;

import com.example.leiriajeansamsi.Modelo.MetodoPagamento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MetodosPagamentoJsonParser {
    public static List<MetodoPagamento> parserJsonMetodosPagamento(JSONArray response) {
        List<MetodoPagamento> metodosPagamento = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonMetodo = response.getJSONObject(i);
                MetodoPagamento metodo = new MetodoPagamento();
                metodo.setId(jsonMetodo.getInt("id"));
                metodo.setNome(jsonMetodo.getString("nome"));
                metodosPagamento.add(metodo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return metodosPagamento;
    }
}