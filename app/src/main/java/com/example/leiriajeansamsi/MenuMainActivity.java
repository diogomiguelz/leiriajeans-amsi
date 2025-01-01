package com.example.leiriajeansamsi;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

public class MenuMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private String email="Sem email";
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.ndOpen, R.string.ndClose);
        toggle.syncState();
        drawer.addDrawerListener(toggle);
        carregarCabecalho();

        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager=getSupportFragmentManager();
        carregarFragmentoInicial();
    }

    private boolean carregarFragmentoInicial() {
        Menu menu=navigationView.getMenu();
        MenuItem item=menu.getItem(0);
        item.setChecked(true);
        return onNavigationItemSelected(item);
    }

    private void carregarCabecalho() {
        email = getIntent().getStringExtra(LoginActivity.EMAIL);
        //guardar email no shared
        SharedPreferences sharedPrefUser=getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
        if(email!=null){
            SharedPreferences.Editor editUser=sharedPrefUser.edit();
            editUser.putString("EMAIL", email);
            editUser.apply();
        } else {
            View hView = navigationView.getHeaderView(0);
            TextView tvEmail = hView.findViewById(R.id.tvEmail);
            tvEmail.setText(email);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment=null;
        if (item.getItemId() == R.id.navLista) {
            setTitle(item.getTitle());
            //fragment=new EstaticoFragment();
            fragment=new ListaLivroFragment();
        } else if (item.getItemId() == R.id.navGrelha) {
            setTitle(item.getTitle());
        } else
            enviarEmail();

        if (fragment!=null)
            fragmentManager.beginTransaction().replace(R.id.contentFragment,fragment).commit();

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void enviarEmail() {
        //TODO: intent implicito-> ACTION SEND (ver android developers)
    }
}