package com.example.leiriajeansamsi.utils;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.leiriajeansamsi.Modelo.Utilizador;

public class LoginJsonParser {

    public static Utilizador parserJsonLogin(JSONObject response) {
        Utilizador utilizador = null;
        try {
            Log.d("LoginParser", "Parsing response: " + response.toString());
            
            JSONObject loginJSON = new JSONObject(response.toString());
            int id = loginJSON.getInt("id");
            String username = loginJSON.getString("username");
            String auth_key = loginJSON.getString("auth_key");
            String password_hash = loginJSON.getString("password_hash");
            String password_reset_token = "";
            String email = loginJSON.getString("email");
            String status = loginJSON.getString("status");
            String created_at = loginJSON.getString("created_at");
            String updated_at = loginJSON.getString("updated_at");
            String verification_token = "";

            utilizador = new Utilizador(id, username, "", "", email, "",
                    "","", "", auth_key, password_hash, password_reset_token,
                    status, created_at, updated_at, verification_token);
            
            Log.d("LoginParser", "Utilizador criado com sucesso: " + utilizador.getUsername());
        } catch (JSONException e) {

            Log.e("LoginParser", "Erro ao fazer parse: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return utilizador;
    }

    public static Utilizador parserJsonGetUtilizadorData(JSONObject response) {
        Utilizador utilizador = null;
        try {
            JSONObject loginJSON = new JSONObject(response.toString());
            int id = loginJSON.getInt("id");
            String nome = loginJSON.getString("nome");
            String codpostal = loginJSON.getString("codpostal");
            String localidade = loginJSON.getString("localidade");
            String rua = loginJSON.getString("rua");
            String nif = loginJSON.getString("nif");
            String telefone = loginJSON.getString("telefone");

            utilizador = new Utilizador(id, "", nome, codpostal, rua,
                    localidade, telefone, nif,"",
                    "", "", "");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return utilizador;
    }
}

