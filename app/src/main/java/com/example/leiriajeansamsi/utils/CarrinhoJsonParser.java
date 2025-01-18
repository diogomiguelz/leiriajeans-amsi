package com.example.leiriajeansamsi.utils;

import android.util.Log;

import com.example.leiriajeansamsi.Modelo.Carrinho;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CarrinhoJsonParser {
    private static final String TAG = "CarrinhoJsonParser";

    public static Carrinho parserJsonCarrinho(JSONObject response) throws JSONException {
        Log.d(TAG, "Parsing carrinho: " + response.toString());

        // Se receber uma resposta com errors, retorna null para criar um novo carrinho
        if (response.has("errors")) {
            return null;
        }

        Carrinho carrinho = new Carrinho();
        carrinho.setId(response.getInt("id"));
        carrinho.setUserdataId(response.getInt("userdata_id"));
        carrinho.setTotal((float) response.optDouble("total", 0.0));
        carrinho.setIvatotal((float) response.optDouble("ivatotal", 0.0));

        return carrinho;
    }

    public static ArrayList<Carrinho> parserJsonCarrinhos(JSONArray response) {
        ArrayList<Carrinho> carrinhos = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject carrinhoJSON = response.getJSONObject(i);
                Carrinho carrinho = parserJsonCarrinho(carrinhoJSON);
                carrinhos.add(carrinho);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Erro ao fazer parse dos carrinhos: " + e.getMessage());
        }
        return carrinhos;
    }

    public static Carrinho criarCarrinho(JSONObject response) throws JSONException {
        return parserJsonCarrinho(response);
    }

}