package com.example.leiriajeansamsi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leiriajeansamsi.Modelo.SingletonProdutos;
import com.example.leiriajeansamsi.Modelo.Utilizador;
import com.example.leiriajeansamsi.listeners.LoginListener;
import com.example.leiriajeansamsi.listeners.SignupListener;

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

        SingletonProdutos.getInstance(this).setLoginListener(this); // Configurando o LoginListener
        SingletonProdutos.getInstance(this).setSignupListener(this); // Configurando o SignupListener
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
            Log.d("LoginActivity", "Login bem sucedido, a navegar para MenuMainActivity");
            Intent intent = new Intent(this, MenuMainActivity.class);
            intent.putExtra(TOKEN, utilizador.getAuth_key());
            intent.putExtra(USERNAME, utilizador.getUsername());
            startActivity(intent);
            finish();
        } else {
            Log.e("LoginActivity", "Login falhou - utilizador ou token nulo");
            Toast.makeText(this, "Falha no login: Credenciais inválidas", Toast.LENGTH_SHORT).show();
        }
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
            // Verifica se o campo auth_key existe, o que pode indicar que o registro foi bem-sucedido
            if (newUser.getAuth_key() != null && !newUser.getAuth_key().isEmpty()) {
                // Cadastro realizado com sucesso
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                // Aqui você pode redirecionar o utilizador para outra tela (como a tela de login ou tela principal)
            } else {
                // Falha no cadastro (sem auth_key)
                Toast.makeText(this, "Falha no cadastro. Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Caso o newUser seja nulo (o que pode ocorrer em caso de erro)
            Toast.makeText(this, "Erro no cadastro. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }
}
