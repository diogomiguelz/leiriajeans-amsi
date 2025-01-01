package com.example.leiriajeansamsi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.leiriajeansamsi.listeners.LoginListener;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements LoginListener {

    public static final String EMAIL = "EMAIL";
    private EditText etEmail, etPassword;

    public static int MIN_PASS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etEmail.setText("aulas@amsi.pt");
        etPassword.setText("aulas-amsi");
        SingletonGestorLivros.getInstance(getApplicationContext()).setLoginListener(this);
    }


    public void onClickLogin(View view) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (!IsEmailValido(email)) {
            etEmail.setError("Formato de email inválido");
            return;
        }

        if (!IsPasswordValida(password)) {
            etPassword.setError("Não tem o nº mínimo de caracteres");
            return;
        }

        SingletonGestorLivros.getInstance(getApplicationContext()).loginAPI(email, password, this);
    }


    public boolean IsEmailValido(String email) {
        if (email==null)
            return false;
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean IsPasswordValida(String pass) {
        if(pass==null) {
            return false;
        }
        return pass.length()>=MIN_PASS;
    }

    @Override
    protected void onStart(){
        super.onStart();
        System.out.println("OnStart: a atv prestes a tornar-se visivel");
    }

    @Override
    protected void onResume(){
        super.onResume();
        System.out.println("OnResume: a atv já se tornou visivel");
    }

    @Override
    protected void onPause(){
        super.onPause();
        System.out.println("OnResuma: outra atv está a ser chamada");
    }

    @Override
    protected void onStop(){
        super.onStop();
        System.out.println("OnStop: a atv já não está visivel");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        System.out.println("OnDestroy: a atv destruida e recursos libertados");
    }

    @Override
    public void onUpdateLogin(String token) {
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Login falhou. Por favor, tente novamente.", Toast.LENGTH_LONG).show();
            return;
        }
        else
            Toast.makeText(this, token, Toast.LENGTH_LONG).show();

        // Salve o token ou prossiga para a próxima tela
        Intent intent = new Intent(this, MenuMainActivity.class);
        intent.putExtra(LoginActivity.EMAIL, etEmail.getText().toString());
        startActivity(intent);
        finish();
    }

}