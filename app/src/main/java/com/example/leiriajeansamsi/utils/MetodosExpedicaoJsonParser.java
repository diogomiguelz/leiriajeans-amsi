package com.example.leiriajeansamsi.utils;

import com.example.leiriajeansamsi.Modelo.MetodoExpedicao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MetodosExpedicaoJsonParser {
    public static List<MetodoExpedicao> parserJsonMetodosExpedicao(JSONArray response) {
        List<MetodoExpedicao> metodosExpedicao = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonMetodo = response.getJSONObject(i);
                MetodoExpedicao metodo = new MetodoExpedicao();
                metodo.setId(jsonMetodo.getInt("id"));
                metodo.setNome(jsonMetodo.getString("nome"));
                metodo.setCusto((float) jsonMetodo.getDouble("custo"));
                metodo.setPrazoEntrega(jsonMetodo.getInt("prazo_entrega"));
                metodo.setDescricao(jsonMetodo.optString("descricao", ""));
                metodosExpedicao.add(metodo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return metodosExpedicao;
    }
}