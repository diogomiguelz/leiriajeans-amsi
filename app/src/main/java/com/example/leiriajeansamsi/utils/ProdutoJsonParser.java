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

                // Adaptação para o modelo com os novos campos
                int id = produtoJSON.getInt("id");
                String nome = produtoJSON.getString("nome");
                String descricao = produtoJSON.getString("descricao");
                String cor = produtoJSON.getString("cor"); // Novo campo 'cor'
                int stock = produtoJSON.getInt("stock"); // Novo campo 'stock'
                float preco = (float) produtoJSON.getDouble("preco");
                int iva = produtoJSON.getInt("iva");
                String categoria = produtoJSON.getString("categoria");
                String imagem = produtoJSON.getString("imagens");

                // criar o Produto com os campos
                Produto produto = new Produto(id, iva, stock, nome, descricao, categoria, imagem, cor, preco);
                produtos.add(produto);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return produtos;
    }

    public static Produto parserJsonProduto(String response) {
        Produto produto = null;
        try {
            JSONObject produtoJSON = new JSONObject(response);

            // Adaptação para o modelo com os novos campos
            int id = produtoJSON.getInt("id");
            String nome = produtoJSON.getString("nome");
            String descricao = produtoJSON.getString("descricao");
            String cor = produtoJSON.getString("cor"); // Novo campo 'cor'
            int stock = produtoJSON.getInt("stock"); // Novo campo 'stock'
            float preco = (float) produtoJSON.getDouble("preco");
            int iva = produtoJSON.getInt("iva");
            String categoria = produtoJSON.getString("categoria");
            String imagem = produtoJSON.getString("imagens");

            // Criar o Produto
            produto = new Produto(id, iva, stock, nome, descricao, categoria, imagem, cor, preco);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Log.d("ProdutosJsonParser", "parserJsonProduto: " + produto);
        return produto;
    }

    public static boolean isConnectionInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

}
