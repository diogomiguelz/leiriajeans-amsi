package com.example.leiriajeansamsi;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leiriajeansamsi.Modelo.Fatura;
import com.example.leiriajeansamsi.Modelo.FaturasDBHelper;
import com.example.leiriajeansamsi.Modelo.SingletonProdutos;
import com.example.leiriajeansamsi.adaptadores.ListaFaturasAdaptador;
import com.example.leiriajeansamsi.listeners.FaturaListener;
import com.example.leiriajeansamsi.listeners.FaturasListener;

import java.util.ArrayList;
import java.util.List;

public class FaturasFragment extends Fragment implements FaturasListener, FaturaListener {

    private RecyclerView recyclerFaturas;
    private ListaFaturasAdaptador adaptador;
    private FaturasDBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faturas, container, false);

        dbHelper = new FaturasDBHelper(getContext());
        recyclerFaturas = view.findViewById(R.id.recyclerFaturas);
        recyclerFaturas.setLayoutManager(new LinearLayoutManager(getContext()));

        adaptador = new ListaFaturasAdaptador(this, getContext(), new ArrayList<>());
        recyclerFaturas.setAdapter(adaptador);

        carregarFaturas();

        return view;
    }

    private void carregarFaturas() {
        try {
            int userId = SingletonProdutos.getInstance(getContext()).getUserId(getContext());

            if (isConnectedToInternet()) {
                Log.d("TAG", "Carregando faturas online");
                SingletonProdutos.getInstance(getContext()).getFaturasAPI(getContext(), userId, new FaturasListener() {
                    @Override
                    public void onRefreshListaFatura(ArrayList<Fatura> faturas) {
                        if (faturas == null) {
                            faturas = new ArrayList<>();
                        }
                        adaptador.updateFaturas(faturas);
                        if (faturas.isEmpty()) {
                            Toast.makeText(getContext(), "Nenhuma fatura disponível", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFaturaCriada(Fatura fatura) {
                        // Não necessário implementar
                    }
                });
            } else {
                Log.d("TAG", "Modo offline");
                List<Fatura> faturasOffline = dbHelper.getAllFaturas(userId);
                onRefreshListaFatura(new ArrayList<>(faturasOffline));
                String mensagem = faturasOffline.isEmpty() ? "Sem faturas disponíveis" : "Mostrando dados offline";
                Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("TAG", "Erro ao carregar faturas: " + e.getMessage());
            onRefreshListaFatura(new ArrayList<>());
            Toast.makeText(getContext(), "Erro ao carregar faturas", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        carregarFaturas(); // Recarrega dados quando o fragment volta ao foco
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
        return capabilities != null &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }


    @Override
    public void onRefreshListaFatura(ArrayList<Fatura> faturas) {
        if (faturas == null) {
            faturas = new ArrayList<>();
        }
        if (adaptador != null) {
            adaptador.updateFaturas(faturas);
        }
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
