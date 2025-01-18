package com.example.leiriajeansamsi;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;


public class MenuMainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private String email;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.fragment_container);
        navigationView = findViewById(R.id.navView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.ndOpen, R.string.ndClose);
        toggle.syncState();
        drawer.addDrawerListener(toggle);

        carregarCabecalho();
        fragmentManager = getSupportFragmentManager();
        carregarFragmentoInicial();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private boolean carregarFragmentoInicial() {
        Menu menu = navigationView.getMenu();
        MenuItem item=menu.getItem(0);
        item.setChecked(true);
        return onNavigationItemSelected(item);
    }

    private void carregarCabecalho() {
        Intent intent = getIntent();
        email = intent.getStringExtra(LoginActivity.USERNAME);

        if(email != null){
            View hView = navigationView.getHeaderView(0);
            TextView tvEmail=hView.findViewById(R.id.tvEmail);
            tvEmail.setText(email);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        if (item.getItemId() == R.id.navProdutos) {
            fragment = new ListaProdutosFragment();
            setTitle(item.getTitle());
        }

        if (item.getItemId() == R.id.navCarrinho) {
            fragment = new CarrinhoFragment();
            setTitle(item.getTitle());
        }

        if (item.getItemId() == R.id.navPerfil) {
            fragment = new PerfilFragment();
            setTitle(item.getTitle());
        }

        if (item.getItemId() == R.id.navFaturas) {
            fragment = new FaturasFragment();
            setTitle(item.getTitle());
        } else if (item.getItemId() == R.id.navSobre) {
            fragment = new SobreFragment();
            //setTitle(item.getTitle());
        } else if (item.getItemId() == R.id.navEmail) {
            enviarEmail();
        } else if (item.getItemId() == R.id.navLogout) {
        realizarLogout();
        return true;
    }


        if (fragment != null) fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void enviarEmail() {
        String subject ="LeiriJeans - Cliente";
        String message="**Esclarecimentos da Aplicação, aqui podes descrever o que queres esclarecer**";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");

        String[] recipients = {"2220866@my.ipleiria.pt"};

        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(intent.EXTRA_SUBJECT,subject);
        intent.putExtra(intent.EXTRA_TEXT,message);

        if(intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);
        else
            Toast.makeText(this,"Não tem email configurado!", Toast.LENGTH_LONG).show();
    }

    private void realizarLogout() {
        // Criar um AlertDialog
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Tem certeza que deseja sair?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    // Limpar as SharedPreferences
                    getSharedPreferences("user_prefs", MODE_PRIVATE)
                            .edit()
                            .clear()
                            .apply();

                    // Criar intent para voltar à tela de login
                    Intent intent = new Intent(this, LoginActivity.class);

                    // Limpar a pilha de activities
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    // Iniciar a LoginActivity
                    startActivity(intent);

                    // Finalizar a activity atual
                    finish();
                })
                .setNegativeButton("Não", (dialog, which) -> {
                    // Utilizador cancelou o logout
                    dialog.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}