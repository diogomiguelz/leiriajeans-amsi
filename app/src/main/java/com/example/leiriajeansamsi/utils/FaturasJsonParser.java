package com.example.leiriajeansamsi.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.example.leiriajeansamsi.Modelo.Fatura;
import com.example.leiriajeansamsi.Modelo.Produto;

public class FaturasJsonParser {

    public static ArrayList<Fatura> parserJsonFaturas(JSONArray response) {
        ArrayList<Fatura> faturas = new ArrayList<Fatura>();
        try {
            for (int i = 0; i < response.length(); i++) {
                if (response.get(i) instanceof JSONObject) {
                    JSONObject produtoJSON = (JSONObject) response.get(i);
                    int id = produtoJSON.getInt("id");
                    int userdata_id = produtoJSON.getInt("userdata_id");
                    int metodopagamento = produtoJSON.getInt("metodopagamento");
                    int metodoexpedicao = produtoJSON.getInt("metodoexpedicao");
                    String data = produtoJSON.getString("data");
                    float valortotal = (float) produtoJSON.getDouble("valortotal");
                    String statuspedido = produtoJSON.getString("statuspedido");

                    Fatura fatura = new Fatura(id, metodopagamento, metodoexpedicao, userdata_id, data, valortotal, statuspedido);
                    faturas.add(fatura);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return faturas;


    }
}
