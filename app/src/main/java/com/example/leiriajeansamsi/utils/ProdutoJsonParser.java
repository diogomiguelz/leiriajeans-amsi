package com.example.leiriajeansamsi.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.leiriajeansamsi.Modelo.Produto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProdutoJsonParser {

    public static ArrayList<Produto> parserJsonProdutos(JSONArray response) {
        ArrayList<Produto> produtos = new ArrayList<Produto>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject produtoJSON = (JSONObject) response.get(i);

                int id = produtoJSON.getInt("id");
                String nome = produtoJSON.getString("nome");
                String descricao = produtoJSON.getString("descricao");
                String cor = produtoJSON.optString("cor", "");
                int stock = produtoJSON.optInt("stock", 0);
                float preco = (float) produtoJSON.optDouble("preco", 0.0);
                int iva = produtoJSON.optInt("iva", 0);
                String categoria = produtoJSON.optString("categoria", "");
                String imagem = produtoJSON.optString("imagens", "");
                String sexo = produtoJSON.optString("sexo");

                Produto produto = new Produto(id, iva, stock, nome, descricao, categoria, imagem, cor, sexo, preco);
                produtos.add(produto);
            }
        } catch (JSONException e) {
            Log.e("ProdutoJsonParser", "Erro ao fazer parse: " + e.getMessage());
        }
        return produtos;
    }

    public static Produto parserJsonProduto(String response) {
        Produto produto = null;
        try {
            JSONObject produtoJSON = new JSONObject(response);

            int id = produtoJSON.getInt("id");
            String nome = produtoJSON.getString("nome");
            String descricao = produtoJSON.getString("descricao");
            String cor = produtoJSON.optString("cor", "");
            int stock = produtoJSON.optInt("stock", 0);
            float preco = (float) produtoJSON.optDouble("preco", 0.0);
            int iva = produtoJSON.optInt("iva", 0);
            String categoria = produtoJSON.optString("categoria", "");
            String imagem = produtoJSON.optString("imagens", "");
            String sexo = produtoJSON.optString("sexo", "U"); // U para Unissex como padrão

            produto = new Produto(id, iva, stock, nome, descricao, categoria, imagem, cor, sexo, preco);
        } catch (JSONException e) {
            Log.e("ProdutoJsonParser", "Erro ao fazer parse: " + e.getMessage());
        }

        return produto;
    }


    public static boolean isConnectionInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

}
