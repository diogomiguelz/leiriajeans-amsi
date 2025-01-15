package com.example.leiriajeansamsi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leiriajeansamsi.Modelo.Fatura;
import com.example.leiriajeansamsi.Modelo.SingletonProdutos;
import com.example.leiriajeansamsi.adaptadores.ListaFaturasAdaptador;
import com.example.leiriajeansamsi.listeners.FaturaListener;
import com.example.leiriajeansamsi.listeners.FaturasListener;

import java.util.ArrayList;

public class FaturasFragment extends Fragment implements FaturasListener, FaturaListener {

    private RecyclerView recyclerFaturas;
    private ListaFaturasAdaptador adaptador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faturas, container, false);

        recyclerFaturas = view.findViewById(R.id.recyclerFaturas);
        recyclerFaturas.setLayoutManager(new LinearLayoutManager(getContext()));

        adaptador = new ListaFaturasAdaptador(this, getContext(), new ArrayList<>());
        recyclerFaturas.setAdapter(adaptador);

        SingletonProdutos.getInstance(getContext()).setFaturasListener(this);

        // Obter o ID do utilizador a partir do SingletonProdutos
        int userId = SingletonProdutos.getInstance(getContext()).getUserId(getContext());
        SingletonProdutos.getInstance(getContext()).getFaturasAPI(getContext(), userId, this);

        return view;
    }


    @Override
    public void onRefreshListaFatura(ArrayList<Fatura> faturas) {
        adaptador.updateFaturas(faturas);
    }

    @Override
    public void onFaturaCriada(Fatura fatura) {
        // Handle fatura criada
    }

    @Override
    public void onItemClick(int position, Fatura fatura) {
        DetalhesFaturaFragment fragment = new DetalhesFaturaFragment();
        Bundle args = new Bundle();
        args.putInt("FATURA_ID", fatura.getId());
        fragment.setArguments(args);

        // navega para o fragment
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.navFaturas, fragment)
                .addToBackStack(null)
                .commit();
    }




}
