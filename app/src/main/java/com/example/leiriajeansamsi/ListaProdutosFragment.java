package com.example.leiriajeansamsi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leiriajeansamsi.Modelo.Produto;
import com.example.leiriajeansamsi.Modelo.SingletonProdutos;
import com.example.leiriajeansamsi.adaptadores.ListaProdutosAdaptador;
import com.example.leiriajeansamsi.listeners.ProdutoListener;
import com.example.leiriajeansamsi.listeners.ProdutosListener;

import java.util.ArrayList;

public class ListaProdutosFragment extends Fragment implements ProdutosListener, ProdutoListener {

    private TextView tvNomeProduto, tvPrecoProduto;
    public ArrayList<Produto> listaProdutos;
    private RecyclerView rvProdutos;
    private ListaProdutosAdaptador adapter;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_produtos, container, false);

        // Set the title for the fragment
        getActivity().setTitle("Produtos");

        // Initialize UI components
        rvProdutos = view.findViewById(R.id.listProdutos);
        progressBar = view.findViewById(R.id.progressBarProdutos);

        // Configure RecyclerView
        rvProdutos.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Set up Singleton listener
        SingletonProdutos.getInstance(getContext()).setProdutosListener(this);
        SingletonProdutos.getInstance(getContext()).getAllProdutosAPI(getContext());

        return view;
    }

    @Override
    public void onRefreshListaProdutos(ArrayList<Produto> listaProdutos) {
        // Retrieve query from arguments (if any)
        Bundle args = getArguments();
        if (args != null) {
            String query = args.getString("query");

            if (query != null && listaProdutos.size() > 0) {
                listaProdutos = SingletonProdutos.getInstance(getContext()).getFilteredProdutos(query);
            }
        }

        // Update RecyclerView adapter
        adapter = new ListaProdutosAdaptador(this, getContext(), listaProdutos);
        rvProdutos.setAdapter(adapter);

        // Hide the progress bar
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(int position, Produto product) {

        Context context = getContext();
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;

            // Criar o Fragmento
            DetalhesProdutosFragment detalhesProdutoFragment = new DetalhesProdutosFragment();

            // Passar dados (exemplo do ID do produto)
            Bundle args = new Bundle();
            args.putParcelable(DetalhesProdutosFragment.PRODUTO, product);  // Corrigido aqui
            detalhesProdutoFragment.setArguments(args);

            // Iniciar a transação do fragmento
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

            // Substituir o fragmento atual
            transaction.replace(R.id.fragment_container, detalhesProdutoFragment);  // Substitui o fragmento
            transaction.addToBackStack(null);  // Adicionar à pilha para navegação
            transaction.commit();
        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_geral, menu);
    }
}
