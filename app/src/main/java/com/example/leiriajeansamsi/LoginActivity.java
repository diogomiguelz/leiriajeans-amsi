package com.example.leiriajeansamsi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.leiriajeansamsi.Modelo.Fatura;
import com.example.leiriajeansamsi.Modelo.FaturasDBHelper;
import com.example.leiriajeansamsi.Modelo.Singleton;
import com.example.leiriajeansamsi.Modelo.Utilizador;
import com.example.leiriajeansamsi.listeners.FaturasListener;
import com.example.leiriajeansamsi.listeners.LoginListener;
import com.example.leiriajeansamsi.listeners.SignupListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements LoginListener, SignupListener, FaturasListener {

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    public static final String TOKEN = "token";

    private EditText etUsername, etPassword;
    private final int MIN_PASS = 4;
    private boolean isLoggingIn = false;
    private Utilizador utilizadorAtual;
    private Singleton singleton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

//        if (HasUserToken(this)) {
//            Intent intent = new Intent(this, MenuMainActivity.class);
//            startActivity(intent);
//            finish();
//        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        Singleton.getInstance(this).getUserDataAPI(this, null);
        Singleton.getInstance(this).setLoginListener(this);
        Singleton.getInstance(this).setSignupListener(this);
    }

//    public boolean HasUserToken(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
//        return preferences.getString("user_token", null) != null;
//    }

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

        Singleton.getInstance(this).loginAPI(username, pass, this);
    }

    @Override
    public void onUpdateLogin(Utilizador utilizador) {
        isLoggingIn = false;
        if (utilizador != null && utilizador.getAuth_key() != null) {
            utilizadorAtual = utilizador;
            // Sincroniza as faturas antes de ir para o menu principal
            sincronizarFaturas();
        } else {
            Toast.makeText(this,
                    "Falha no login: Credenciais inválidas",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void sincronizarFaturas() {
        try {
            if (isConnectedToInternet()) {
                Log.d("LoginActivity", "A sincronizar as faturas online");
                Singleton.getInstance(this).setFaturasListener(this);
                Singleton.getInstance(this).getFaturasAPI(this);
            } else {
                Log.d("LoginActivity", "Modo offline");
                goParaMenuPrincipal(utilizadorAtual);
            }
        } catch (Exception e) {
            Log.e("LoginActivity", "Erro na sincronização: " + e.getMessage());
            goParaMenuPrincipal(utilizadorAtual);
        }
    }

    @Override
    public void onRefreshListaFatura(ArrayList<Fatura> faturas) {
        // Guardar as faturas na base dados local se houver
        if (faturas != null) {
            FaturasDBHelper dbHelper = new FaturasDBHelper(this);
            dbHelper.atualizarFaturas(faturas, utilizadorAtual.getId());
        }
        
        // Continua para o menu principal independentemente do resultado
        goParaMenuPrincipal(utilizadorAtual);
    }

    @Override
    public void onFaturaCriada(Fatura fatura) {
        // Não necessário implementar
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
            if (newUser.getAuth_key() != null && !newUser.getAuth_key().isEmpty()) {
                Toast.makeText(this, "Registo realizado com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Falha no registo. Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Erro no registo. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }
}