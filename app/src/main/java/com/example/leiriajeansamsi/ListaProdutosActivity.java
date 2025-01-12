package com.example.leiriajeansamsi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leiriajeansamsi.Modelo.Produto;
import com.example.leiriajeansamsi.Modelo.SingletonProdutos;
import com.example.leiriajeansamsi.adaptadores.ListaProdutosAdaptador;
import com.example.leiriajeansamsi.listeners.ProdutoListener;
import com.example.leiriajeansamsi.listeners.ProdutosListener;

import java.util.ArrayList;

public class ListaProdutosActivity extends AppCompatActivity implements ProdutosListener, ProdutoListener {

    private TextView tvNomeProduto, tvPrecoProduto;
    public ArrayList<Produto> listaProdutos;
    private RecyclerView rvProdutos;
    private ListaProdutosAdaptador adapter;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);


        SingletonProdutos.getInstance(getApplicationContext()).setProdutosListener(this);
        SingletonProdutos.getInstance(getApplicationContext()).getAllProdutosAPI(getApplicationContext());
        rvProdutos = findViewById(R.id.listProdutos);
        rvProdutos.setLayoutManager(new GridLayoutManager(this, 2));


    }

    @Override
    public void onRefreshListaProdutos(ArrayList<Produto> listaProdutos) {


        Intent intent = getIntent();
        if (intent != null) {
            String query = intent.getStringExtra("query");


            if (query != null && listaProdutos.size() > 0) {
                listaProdutos = SingletonProdutos.getInstance(getApplicationContext()).getFilteredProdutos(query);
            }
        }

        rvProdutos = findViewById(R.id.listProdutos);
        adapter = new ListaProdutosAdaptador(this, getApplicationContext(), listaProdutos);
        rvProdutos.setAdapter(adapter);
        progressBar = findViewById(R.id.progressBarProdutos);
        progressBar.setVisibility(ProgressBar.GONE);

    }

    @Override
    public void onItemClick(int position, Produto product) {

        Intent intent = new Intent(this, DetalhesProdutoActivity.class);
        intent.putExtra(DetalhesProdutoActivity.PRODUTO, product);

        startActivity(intent);
    }


}