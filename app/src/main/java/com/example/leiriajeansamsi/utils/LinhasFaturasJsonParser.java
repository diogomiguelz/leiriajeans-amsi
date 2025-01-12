package com.example.leiriajeansamsi.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.example.leiriajeansamsi.Modelo.Fatura;
import com.example.leiriajeansamsi.Modelo.LinhaFatura;

public class LinhasFaturasJsonParser {

    public static ArrayList<LinhaFatura> parserJsonLinhasFaturas(JSONArray response) {
        ArrayList<LinhaFatura> linhaFatura = new ArrayList<LinhaFatura>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject linhaJson = (JSONObject) response.get(i);
                int id = linhaJson.getInt("id");
                int fatura = linhaJson.getInt("fatura");
                int iva = linhaJson.getInt("iva");
                int produto = linhaJson.getInt("produto");
                float precoVenda = (float) linhaJson.getDouble("precoVenda");
                float valorIva = (float) linhaJson.getDouble("valorIva");
                float subTotal = (float) linhaJson.getDouble("subTotal");
                int quantidade = linhaJson.getInt("quantidade");

                LinhaFatura linha = new LinhaFatura(id, fatura, iva, produto, precoVenda, valorIva, subTotal, quantidade);
                linhaFatura.add(linha);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return linhaFatura;


    }


}
