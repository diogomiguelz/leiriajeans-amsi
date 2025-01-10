package com.example.leiriajeansamsi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.example.leiriajeansamsi.MenuMainActivity;
import com.example.leiriajeansamsi.RegistarActivity;

public class LoginActivity extends AppCompatActivity {

    public static final String EMAIL = "email";
    //Declaração
    private EditText etEmail, etPassword;

    private final int MIN_PASS=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        //Inicialização
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);

    }

    private boolean isEmailValido(String email){
        if(email == null)
            return false;

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isPasswordValida(String pass){
        if(pass==null)
            return false;

        return pass.length()>=MIN_PASS;
    }

    public void onClickLogin(View view) {
        String email=etEmail.getText().toString();
        String pass=etPassword.getText().toString();
//        if(!isEmailValido(email) | !isPasswordValida(pass)) {
//            Toast.makeText(this,"Login Inválido!", Toast.LENGTH_SHORT).show();
//            return;
//        }

        Intent intent = new Intent(getApplicationContext(), MenuMainActivity.class);
        intent.putExtra(EMAIL, email);
        startActivity(intent);
        finish();

    }

    public void onClickRegistar(View view) {
        Intent intent = new Intent(getApplicationContext(), RegistarActivity.class);
        startActivity(intent);
    }
}