package com.example.leiriajeansamsi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leiriajeansamsi.Modelo.Fatura;
import com.example.leiriajeansamsi.Modelo.FaturasDBHelper;
import com.example.leiriajeansamsi.Modelo.SingletonProdutos;
import com.example.leiriajeansamsi.Modelo.Utilizador;
import com.example.leiriajeansamsi.listeners.FaturasListener;
import com.example.leiriajeansamsi.listeners.LoginListener;
import com.example.leiriajeansamsi.listeners.SignupListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements LoginListener, SignupListener {

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    public static final String TOKEN = "token";

    private EditText etUsername, etPassword;
    private final int MIN_PASS = 4;
    private boolean isLoggingIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        SingletonProdutos.getInstance(this).setLoginListener(this); // A configurar o LoginListener
        SingletonProdutos.getInstance(this).setSignupListener(this); // A configurar o SignupListener
    }


    private boolean isEmailValido(String email) {
        if (email == null) return false;
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValida(String pass) {
        if (pass == null) return false;
        return pass.length() >= MIN_PASS;
    }

    private boolean isUsernameValido(String username) {
        if (username == null) return false;
        String usernamePattern = "^[a-zA-Z0-9_]{3,20}$";
        return username.matches(usernamePattern);
    }

    public void onClickLogin(View view) {
        if (isLoggingIn) {
            return;
        }
        isLoggingIn = true;

        String username = etUsername.getText().toString();
        String pass = etPassword.getText().toString();

        Log.d("LoginActivity", "Username: " + username + ", Password: " + pass);

        if (!isUsernameValido(username)) {
            etUsername.setError("Username Inválido!");
            isLoggingIn = false;
            return;
        }
        if (!isPasswordValida(pass)) {
            etPassword.setError("Password Inválida!");
            isLoggingIn = false;
            return;
        }

        SingletonProdutos.getInstance(this).loginAPI(username, pass, this);
    }

    @Override
    public void onUpdateLogin(Utilizador utilizador) {
        isLoggingIn = false;
        if (utilizador != null && utilizador.getAuth_key() != null) {
            sincronizarFaturas(utilizador);
        } else {
            Toast.makeText(this,
                    "Falha no login: Credenciais inválidas",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void sincronizarFaturas(Utilizador utilizador) {
        try {
            int userId = utilizador.getId();

            if (isConnectedToInternet()) {
                Log.d("LoginActivity", "Sincronizando faturas online para utilizador: " + userId);

                SingletonProdutos.getInstance(this).getFaturasAPI(this, userId,
                        new FaturasListener() {
                            @Override
                            public void onRefreshListaFatura(ArrayList<Fatura> faturas) {
                                if (faturas == null) {
                                    faturas = new ArrayList<>();
                                }
                                // Continua mesmo sem faturas
                                goParaMenuPrincipal(utilizador);
                            }

                            @Override
                            public void onFaturaCriada(Fatura fatura) {
                                // Não necessário implementar
                            }
                        });

                // Importante: Adicionar este código para garantir que o usuário entre mesmo com erro
                goParaMenuPrincipal(utilizador);
            } else {
                Log.d("LoginActivity", "Modo offline");
                goParaMenuPrincipal(utilizador);
            }
        } catch (Exception e) {
            Log.e("LoginActivity", "Erro na sincronização: " + e.getMessage());
            // Mesmo com erro, permite entrar
            goParaMenuPrincipal(utilizador);
        }
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
        return capabilities != null &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }

    private void goParaMenuPrincipal(Utilizador utilizador) {
        Intent intent = new Intent(this, MenuMainActivity.class);
        intent.putExtra(TOKEN, utilizador.getAuth_key());
        intent.putExtra(USERNAME, utilizador.getUsername());
        startActivity(intent);
        finish();
    }


    public void onClickRegistar(View view) {
        Intent intent = new Intent(getApplicationContext(), RegistarActivity.class);
        startActivity(intent);
    }

    public void onClickImageButton(View view) {
        Intent intent = new Intent(getApplicationContext(), DefinicoesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onUpdateSignup(Utilizador newUser) {
        if (newUser != null) {
            // Verifica se o campo auth_key existe e não está vazio
            if (newUser.getAuth_key() != null && !newUser.getAuth_key().isEmpty()) {
                // Login realizado com sucesso
                Toast.makeText(this, "Registo realizado com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                // Falha no Login (sem auth_key)
                Toast.makeText(this, "Falha no registo. Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Caso o newUser seja nulo (o que pode ocorrer em caso de erro)
            Toast.makeText(this, "Erro no registo. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }
}
