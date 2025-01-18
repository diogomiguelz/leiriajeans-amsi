package com.example.leiriajeansamsi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leiriajeansamsi.Modelo.LinhaCarrinho;
import com.example.leiriajeansamsi.Modelo.SingletonProdutos;
import com.example.leiriajeansamsi.adaptadores.LinhaCarrinhoAdaptador;
import com.example.leiriajeansamsi.listeners.LinhaCarrinhoListener;

import java.util.ArrayList;
import java.util.List;

public class CarrinhoFragment extends Fragment implements LinhaCarrinhoListener {

    private RecyclerView recyclerLinhasCarrinho;
    private LinhaCarrinhoAdaptador adapter;
    private Button btnProceder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carrinho, container, false);

        recyclerLinhasCarrinho = view.findViewById(R.id.recyclerLinhasCarrinho);
        recyclerLinhasCarrinho.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new LinhaCarrinhoAdaptador(getContext(), new ArrayList<>(), this);
        recyclerLinhasCarrinho.setAdapter(adapter);

        btnProceder = view.findViewById(R.id.btnProceder);
        btnProceder.setEnabled(false);

        // Chama o metodo para associar ou criar o carrinho ao inicializar o fragmento
        SingletonProdutos.getInstance(getContext()).getCarrinhoAPI(getContext());

        // Define o listener para atualizar a lista de linhas do carrinho
        SingletonProdutos.getInstance(getContext()).setLinhasCarrinhoListener(linhas -> {
            if (adapter != null) {
                adapter.updateData(linhas);
                btnProceder.setEnabled(linhas.size() > 0);
                if(linhas.size() > 0){
                    btnProceder.setText("Proceder para o checkout");
                } else {
                    btnProceder.setText("Carrinho vazio");
                    Toast.makeText(getContext(), "Carrinho vazio", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnProceder = view.findViewById(R.id.btnProceder);
        btnProceder.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CheckoutActivity.class);
            startActivity(intent);
        });


        return view;
    }




    @Override
    public void onItemUpdate() {
        SingletonProdutos singleton = SingletonProdutos.getInstance(getContext());
        singleton.getLinhasCarrinhoAPI(getContext());
    }

    @Override
    public void atualizarCarrinho(List<LinhaCarrinho> linhas) {
        // Atualiza os dados do adaptador com a nova lista de linhas do carrinho
        if (adapter != null) {
            adapter.updateData(linhas);
        }
    }
}
