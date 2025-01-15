package com.example.leiriajeansamsi.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.example.leiriajeansamsi.Modelo.Carrinho;

public class CarrinhoJsonParser {

    // Método para parser de um único carrinho (JSON)
    public static Carrinho parserJsonCarrinho(JSONObject response) {
        Carrinho carrinho = null;
        try {
            // Parsing do JSON para os atributos do Carrinho
            int id = response.getInt("id");
            int userdataId = response.getInt("userdataId");
            int produto = response.getInt("produto");
            float ivatotal = (float) response.getDouble("ivatotal");
            float total = (float) response.getDouble("total");

            // cria um objeto Carrinho com os dados extraídos
            carrinho = new Carrinho(id, userdataId, produto, ivatotal, total);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("CarrinhoJsonParser", "parserJsonCarrinho: " + carrinho);
        return carrinho;
    }

    // Método para parser de uma lista de carrinhos (JSONArray)
    public static ArrayList<Carrinho> parserJsonCarrinhos(JSONArray response) {
        ArrayList<Carrinho> carrinhos = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject carrinhoJSON = response.getJSONObject(i);
                // Parsing dos dados de cada carrinho
                int id = carrinhoJSON.getInt("id");
                int userdataId = carrinhoJSON.getInt("userdataId");
                int produto = carrinhoJSON.getInt("produto");
                float ivatotal = (float) carrinhoJSON.getDouble("ivatotal");
                float total = (float) carrinhoJSON.getDouble("total");

                // A adicionar o carrinho à lista
                Carrinho carrinho = new Carrinho(id, userdataId, produto, ivatotal, total);
                carrinhos.add(carrinho);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Log.d("CarrinhoJsonParser", "parserJsonCarrinhos: " + carrinhos);
        return carrinhos;
    }
}
