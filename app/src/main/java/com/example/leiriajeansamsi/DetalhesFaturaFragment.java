package com.example.leiriajeansamsi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.leiriajeansamsi.Modelo.Fatura;
import com.example.leiriajeansamsi.Modelo.LinhaFatura;
import com.example.leiriajeansamsi.Modelo.SingletonProdutos;
import com.example.leiriajeansamsi.adaptadores.LinhasFaturasAdaptador;
import com.example.leiriajeansamsi.listeners.LinhasFaturasListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class DetalhesFaturaFragment extends Fragment implements LinhasFaturasListener {
    private RecyclerView recyclerLinhasCheckout;
    private LinhasFaturasAdaptador adapter;
    private TextView tvDataFatura, tvEstadoFatura, tvTotalFatura;
    private int faturaId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalhes_fatura, container, false);

        if (getArguments() != null) {
            faturaId = getArguments().getInt("FATURA_ID", -1);
        }

        initViews(view);
        carregarDetalhesFatura();

        return view;
    }

    private void initViews(View view) {
        recyclerLinhasCheckout = view.findViewById(R.id.recyclerLinhasCheckout);
        tvDataFatura = view.findViewById(R.id.tvDataFatura);
        tvEstadoFatura = view.findViewById(R.id.tvEstadoFatura);
        tvTotalFatura = view.findViewById(R.id.tvTotalFatura);

        recyclerLinhasCheckout.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void carregarDetalhesFatura() {
        if (faturaId != -1) {
            SingletonProdutos.getInstance(getContext()).setLinhasFaturasListener(this);
            SingletonProdutos.getInstance(getContext()).getLinhasFaturasAPI(getContext(), faturaId, this);

            Fatura fatura = SingletonProdutos.getInstance(getContext()).getFaturaById(faturaId);
            if (fatura != null) {
                tvDataFatura.setText("Data: " + fatura.getData());
                tvEstadoFatura.setText("Estado: " + fatura.getStatusPedido());
                tvTotalFatura.setText(String.format("Total: %.2f €", fatura.getValorTotal()));
            }
        }
    }

    @Override
    public void onRefreshListaLinhasFaturas(int faturaId, ArrayList<LinhaFatura> linhasFatura) {
        if (linhasFatura != null && getContext() != null) {
            adapter = new LinhasFaturasAdaptador(getContext(), linhasFatura); // Adicionei o context
            recyclerLinhasCheckout.setAdapter(adapter);
        }
    }

    @Override
    public void onLinhaFaturaCriada(LinhaFatura linhaFatura) {
        // Implementação necessária do metodo abstrato
        // Pode ficar vazio se não precisar usar
    }
}
