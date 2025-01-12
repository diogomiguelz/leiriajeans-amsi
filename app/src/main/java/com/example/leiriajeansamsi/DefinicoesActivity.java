package com.example.leiriajeansamsi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.leiriajeansamsi.databinding.ActivityDefinicoesBinding;

public class DefinicoesActivity extends AppCompatActivity {

    private ActivityDefinicoesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDefinicoesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.confirmarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String apiText = binding.apiTxt.getText().toString();

                saveTextToSharedPreferences(apiText);

                Toast.makeText(DefinicoesActivity.this, "IP Confirmado: " + apiText, Toast.LENGTH_SHORT).show();
                navigateToIntroActivity();
            }
        });
    }

    private void saveTextToSharedPreferences(String apiText) {
        // Use SharedPreferences to save the text
        SharedPreferences preferences = getSharedPreferences("api_url", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("API", apiText);
        editor.apply();
    }


    private void navigateToIntroActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        // Finish the current activity if you don't want to go back to it
        finish();
    }


}
