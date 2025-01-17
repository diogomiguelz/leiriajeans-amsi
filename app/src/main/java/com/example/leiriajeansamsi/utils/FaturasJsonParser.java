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
        Log.d("FaturasJsonParser", "A parsear resposta: " + jsonResponse.toString());

        // Não precisa mais tentar converter o objeto inteiro
        Fatura fatura = new Fatura();

        fatura.setId(jsonResponse.getInt("id"));
        fatura.setMetodoPagamentoId(jsonResponse.optInt("metodopagamento_id", 0));
        fatura.setMetodoExpedicaoId(jsonResponse.optInt("metodoexpedicao_id", 0));
        fatura.setData(jsonResponse.optString("data", "")); // Trata data como string diretamente
        fatura.setValorTotal(jsonResponse.isNull("valorTotal") ? 0 : (float) jsonResponse.optDouble("valorTotal", 0.0));

        // Tratamento do status
        String statusStr = jsonResponse.optString("statuspedido", "pendente").toLowerCase();
        fatura.setStatusPedidoFromString(statusStr); // Usa o metodo que você já tem na classe Fatura

        Log.d("FaturasJsonParser", "Fatura parseada com sucesso: " + fatura);
        return fatura;
    }

    public static ArrayList<Fatura> parserJsonFaturas(JSONArray jsonFaturas) {
        ArrayList<Fatura> faturas = new ArrayList<>();

        if (jsonFaturas == null) {
            Log.e(TAG, "Array JSON de faturas é nulo");
            return faturas;
        }

        Log.d(TAG, "A iniciar parse de " + jsonFaturas.length() + " faturas");

        for (int i = 0; i < jsonFaturas.length(); i++) {
            try {
                JSONObject jsonFatura = jsonFaturas.getJSONObject(i);
                Log.d(TAG, "A processar fatura " + (i + 1) + ": " + jsonFatura.toString());

                Fatura fatura = parseFatura(jsonFatura);
                faturas.add(fatura);
                Log.d(TAG, "Fatura " + (i + 1) + " adicionada com sucesso");
            } catch (JSONException e) {
                Log.e(TAG, "Erro ao processar fatura " + (i + 1) + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        Log.d(TAG, "Total de faturas processadas com sucesso: " + faturas.size());
        return faturas;
    }

    // Metodo auxiliar para logging
    private static void logStatusPedido(String statusStr, JSONObject jsonFatura) {
        Log.d(TAG, "Status recebido da API: " + statusStr);
        Log.d(TAG, "JSON completo da fatura: " + jsonFatura.toString());
    }
}

