package com.example.leiriajeansamsi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvEmail;
    private String email;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

        // Configurar ActionBar inicial
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Bem-vindo");
        }

        tvEmail = findViewById(R.id.tvEmail);
        Intent intent = getIntent();
        email = intent.getStringExtra(LoginActivity.EMAIL);
        if (email != null) {
            tvEmail.setText(email);
        } else {
            tvEmail.setText("Sem email");
        }
    }

    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void onClickEmail(View view) {
        // TODO: intent implícito ACTION_SEND
    }
}
