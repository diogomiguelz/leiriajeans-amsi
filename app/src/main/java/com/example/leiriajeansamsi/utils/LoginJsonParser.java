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

    public static Utilizador parserJsonGetUtilizadorData(JSONObject userForm) {
        try {
            Log.d("DEBUG_PARSER", "Iniciando parsing do userForm: " + userForm.toString());

            Utilizador utilizador = new Utilizador();

            // Os nomes das chaves devem corresponder exatamente aos do JSON recebido
            if (!userForm.isNull("nome")) {
                utilizador.setNome(userForm.getString("nome"));
            }
            if (!userForm.isNull("telefone")) {
                utilizador.setTelefone(userForm.getString("telefone"));
            }
            if (!userForm.isNull("nif")) {
                utilizador.setNif(userForm.getString("nif"));
            }
            if (!userForm.isNull("rua")) {
                utilizador.setRua(userForm.getString("rua"));
            }
            if (!userForm.isNull("localidade")) {
                utilizador.setLocalidade(userForm.getString("localidade"));
            }
            if (!userForm.isNull("codpostal")) {
                utilizador.setCodpostal(userForm.getString("codpostal"));
            }

            Log.d("DEBUG_PARSER", "Parsing concluído com sucesso:" +
                    "\nNome: " + utilizador.getNome() +
                    "\nTelefone: " + utilizador.getTelefone() +
                    "\nNIF: " + utilizador.getNif() +
                    "\nRua: " + utilizador.getRua() +
                    "\nLocalidade: " + utilizador.getLocalidade() +
                    "\nCódigo Postal: " + utilizador.getCodpostal());

            return utilizador;
        } catch (JSONException e) {
            Log.e("DEBUG_PARSER", "Erro no parsing: " + e.getMessage());
            Log.e("DEBUG_PARSER", "Stack trace: ", e);
            return null;
        }
    }
}

