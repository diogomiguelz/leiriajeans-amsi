package com.example.leiriajeansamsi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leiriajeansamsi.Modelo.LinhaCarrinho;
import com.example.leiriajeansamsi.Modelo.SingletonProdutos;
import com.example.leiriajeansamsi.adaptadores.LinhaCarrinhoAdaptador;
import com.example.leiriajeansamsi.listeners.LinhaCarrinhoListener;
import com.example.leiriajeansamsi.listeners.LinhasCarrinhosListener;

import java.util.ArrayList;
import java.util.List;

public class CarrinhoFragment extends Fragment implements LinhaCarrinhoListener, LinhasCarrinhosListener {

    private RecyclerView recyclerLinhasCarrinho;
    private LinhaCarrinhoAdaptador adapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_carrinho, container, false);

        recyclerLinhasCarrinho = view.findViewById(R.id.recyclerLinhasCarrinho);
        recyclerLinhasCarrinho.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new LinhaCarrinhoAdaptador(getContext(), new ArrayList<>());
        recyclerLinhasCarrinho.setAdapter(adapter);

        SingletonProdutos.getInstance(getContext()).setLinhasCarrinhoListener(this);
        SingletonProdutos.getInstance(getContext()).getLinhasCarrinhoAPI(getContext());

        return view;
    }

    @Override
    public void onItemUpdate() {
        SingletonProdutos.getInstance(getContext()).getLinhasCarrinhoAPI(getContext());
    }

    @Override
    public void atualizarCarrinho(List<LinhaCarrinho> linhas) {
        if (adapter != null) {
            adapter.updateData(linhas);
        }
    }

    @Override
    public void onRefreshListaLinhasCarrinhos(ArrayList<LinhaCarrinho> listaLinhaCarrinho) {
        if (adapter != null) {
            adapter.updateData(listaLinhaCarrinho);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getContext() != null) {
            SingletonProdutos.getInstance(getContext()).getLinhasCarrinhoAPI(getContext());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getContext() != null) {
            SingletonProdutos.getInstance(getContext()).setLinhasCarrinhoListener(null);
        }
    }
}
