package com.example.leiriajeansamsi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
    private TextView tvTotalFaturas, tvPendentes, tvPagas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faturas, container, false);

        tvTotalFaturas = view.findViewById(R.id.tvTotalFaturas);
        tvPendentes = view.findViewById(R.id.tvPendentes);
        tvPagas = view.findViewById(R.id.tvPagas);

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
            if (isConnectedToInternet()) {
                Log.d("TAG", "A carregar faturas online");
                SingletonProdutos.getInstance(getContext()).setFaturasListener(this);
                SingletonProdutos.getInstance(getContext()).getFaturasAPI(getContext());
            } else {
                Log.d("TAG", "Modo offline");
                int userId = SingletonProdutos.getInstance(getContext()).getUserId(getContext());
                List<Fatura> faturasOffline = dbHelper.getAllFaturas(userId);
                ArrayList<Fatura> faturas = new ArrayList<>(faturasOffline);
                onRefreshListaFatura(faturas);
                contarFaturas(faturas);

                String mensagem = faturasOffline.isEmpty() ? "Sem faturas disponíveis" : "Mostrar dados offline";
                Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("TAG", "Erro ao carregar faturas: " + e.getMessage());
            ArrayList<Fatura> faturasVazias = new ArrayList<>();
            onRefreshListaFatura(faturasVazias);
            contarFaturas(faturasVazias);
            Toast.makeText(getContext(), "Erro ao carregar faturas", Toast.LENGTH_SHORT).show();
        }
    }


    private void contarFaturas(ArrayList<Fatura> listaFaturas) {
        Log.d("Contadores", "Iniciar contagem de faturas");

        if (listaFaturas == null) {
            Log.d("Contadores", "Lista de faturas é null");
            return;
        }

        Log.d("Contadores", "Tamanho da lista: " + listaFaturas.size());

        int totalFaturas = listaFaturas.size();
        int faturasPagas = 0;
        int faturasPendentes = 0;

        // Conta as faturas por status
        for (Fatura fatura : listaFaturas) {
            if (fatura.getStatusPedido() != null) {  // Verificar se status não é null
                Log.d("Contadores", "Status da fatura " + fatura.getId() + ": " + fatura.getStatusPedido());
                switch (fatura.getStatusPedido()) {
                    case pago:
                        faturasPagas++;
                        break;
                    case pendente:
                        faturasPendentes++;
                        break;
                }
            }
        }

        // Atualiza os TextViews com as contagens
        if (tvTotalFaturas != null) {
            tvTotalFaturas.setText(String.valueOf(totalFaturas));
            Log.d("Contadores", "tvTotalFaturas atualizado: " + totalFaturas);
        } else {
            Log.e("Contadores", "tvTotalFaturas é null");
        }

        if (tvPendentes != null) {
            tvPendentes.setText(String.valueOf(faturasPendentes));
            Log.d("Contadores", "tvPendentes atualizado: " + faturasPendentes);
        } else {
            Log.e("Contadores", "tvPendentes é null");
        }

        if (tvPagas != null) {
            tvPagas.setText(String.valueOf(faturasPagas));
            Log.d("Contadores", "tvPagas atualizado: " + faturasPagas);
        } else {
            Log.e("Contadores", "tvPagas é null");
        }

        // Log para debug
        Log.d("Faturas", "Total: " + totalFaturas +
                " | Pagas: " + faturasPagas +
                " | Pendentes: " + faturasPendentes);
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

            contarFaturas(faturas);
        }
    }

    @Override
    public void onFaturaCriada(Fatura fatura) {
        // Handle fatura criada
    }

    @Override
    public void onItemClick(int position, Fatura fatura) {

        Context context = getContext();
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;

            DetalhesFaturaFragment detalhesFaturaFragment = new DetalhesFaturaFragment();

            Bundle args = new Bundle();
            args.putInt("FATURA_ID", fatura.getId());
            detalhesFaturaFragment.setArguments(args);

            // Iniciar a transação do fragmento
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

            // Substituir o fragmento atual
            transaction.replace(R.id.fragment_container, detalhesFaturaFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }

}
