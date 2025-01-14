package com.example.leiriajeansamsi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private TextView tvEmail;
    private String email;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

        tvEmail=findViewById(R.id.tvEmail);
        Intent intent=getIntent();
        email=intent.getStringExtra(com.example.leiriajeansamsi.LoginActivity.EMAIL);
        if(email!=null)
            tvEmail.setText(email);
        else
            tvEmail.setText("Sem email");

        Button btnCarrinho = findViewById(R.id.navCarrinho);
        btnCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new CarrinhoFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    public void onClickEmail(View view) {
        //TODO: intent implicito ACTION_SEND
    }
}
