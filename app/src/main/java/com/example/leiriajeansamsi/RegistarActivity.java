package com.example.leiriajeansamsi;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.leiriajeansamsi.Modelo.SingletonProdutos;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistarActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail, editTextPassword, editTextNome, editTextCodpostal, editTextLocalidade, editTextRua, editTextNif, editTextTelefone;
    private Button buttonSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);

        // Inicializar os componentes de UI
        editTextUsername = findViewById(R.id.etNome);
        editTextEmail = findViewById(R.id.etEmailRegistar);
        editTextPassword = findViewById(R.id.etPassRegistar);
        editTextNome = findViewById(R.id.etNomeUtilizador);
        editTextCodpostal = findViewById(R.id.etCodPostal);
        editTextLocalidade = findViewById(R.id.etLocalidade);
        editTextRua = findViewById(R.id.etRua);
        editTextNif = findViewById(R.id.etNif);
        editTextTelefone = findViewById(R.id.etTelefone);
        buttonSignup = findViewById(R.id.btnRegistar);

        // Configurar o botão de signup
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obter os dados inseridos pelo utilizador
                String username = editTextUsername.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String nome = editTextNome.getText().toString();
                String codpostal = editTextCodpostal.getText().toString();
                String localidade = editTextLocalidade.getText().toString();
                String rua = editTextRua.getText().toString();
                String nif = editTextNif.getText().toString();
                String telefone = editTextTelefone.getText().toString();

                // Validar se os campos estão preenchidos
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || nome.isEmpty() || codpostal.isEmpty() || localidade.isEmpty() || rua.isEmpty() || nif.isEmpty() || telefone.isEmpty()) {
                    Toast.makeText(RegistarActivity.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Enviar os dados para a API
                signupUser(username, email, password, nome, codpostal, localidade, rua, nif, telefone);
            }
        });
    }

    private void signupUser(String username, String email, String password, String nome, String codpostal, String localidade, String rua, String nif, String telefone) {
        // Criar um objeto JSON com os dados do utilizador
        JSONObject signupData = new JSONObject();
        try {
            signupData.put("username", username);
            signupData.put("email", email);
            signupData.put("password_hash", password);
            signupData.put("nome", nome);
            signupData.put("codpostal", codpostal);
            signupData.put("localidade", localidade);
            signupData.put("rua", rua);
            signupData.put("nif", nif);
            signupData.put("telefone", telefone);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SingletonProdutos.getInstance(RegistarActivity.this).signupAPI(username, password, email, rua, codpostal, localidade, nif, telefone, nome, RegistarActivity.this);
        // Criar uma requisição POST com JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, SingletonProdutos.getInstance(this).mUrlAPISignup(this),

                signupData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Tratar a resposta da API
                        try {
                            if (response.has("message") && response.getString("message").equals("Utilizador e perfil criados com sucesso")) {
                                Toast.makeText(RegistarActivity.this, "Utilizador registado com sucesso!", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(RegistarActivity.this, "Erro ao registar utilizador", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tratar erros de rede ou de requisição
                        Toast.makeText(RegistarActivity.this, "Erro de conexão", Toast.LENGTH_SHORT).show();
                        Log.e("Signup", "Erro: " + error.getMessage());
                    }
                });

        // Adicionar a requisição na fila de requisições do Volley
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

}
