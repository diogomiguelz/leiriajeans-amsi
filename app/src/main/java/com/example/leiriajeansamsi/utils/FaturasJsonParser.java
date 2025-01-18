package com.example.leiriajeansamsi.utils;

import android.util.Log;

import com.example.leiriajeansamsi.Modelo.Fatura;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FaturasJsonParser {
    private static final String TAG = "FaturasJsonParser";

    public static Fatura parseFatura(JSONObject jsonResponse) throws JSONException {
        Log.d(TAG, "Parsing fatura: " + jsonResponse.toString());

        Fatura fatura = new Fatura();
        fatura.setId(jsonResponse.getInt("id"));
        fatura.setMetodoPagamentoId(jsonResponse.optInt("metodopagamento_id", 0));
        fatura.setMetodoExpedicaoId(jsonResponse.optInt("metodoexpedicao_id", 0));
        fatura.setData(jsonResponse.optString("data", ""));

        // Corrigir aqui - usar o campo correto do JSON
        fatura.setValorTotal((float) jsonResponse.optDouble("valorTotal", 0.0));

        // Se houver userdata_id no JSON
        if (jsonResponse.has("userdata_id")) {
            fatura.setUserdata_id(jsonResponse.getInt("userdata_id"));
        }

        fatura.setStatusPedidoFromString(jsonResponse.optString("statuspedido", "pendente"));

        return fatura;
    }


    public static ArrayList<Fatura> parserJsonFaturas(JSONArray response) {
        ArrayList<Fatura> faturas = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonFatura = response.getJSONObject(i);
                Fatura fatura = parseFatura(jsonFatura);
                faturas.add(fatura);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Erro ao fazer parse das faturas: " + e.getMessage());
        }
        return faturas;
    }

    public static Fatura criarFatura(JSONObject response) throws JSONException {
        if (!response.has("success") || !response.getBoolean("success")) {
            throw new JSONException("Resposta inválida");
        }
        return parseFatura(response.getJSONObject("data"));
    }
}