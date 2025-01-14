package com.example.leiriajeansamsi;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import com.example.leiriajeansamsi.Modelo.LinhaCarrinho;
import com.example.leiriajeansamsi.adaptadores.LinhaCarrinhoAdaptador;
import com.example.leiriajeansamsi.utils.LinhaCarrinhoJsonParser;

public class CarrinhoFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinhaCarrinhoAdaptador linhaCarrinhoAdaptador;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carrinho, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewProdutos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Obter os dados JSON
        obterRespostaJson();

        return view;
    }

    private void obterRespostaJson() {
        // Teste com dados falsos
        String jsonString = "[{\"id\":1,\"quantidade\":2,\"carrinho_id\":1,\"produto_id\":1,\"preco_venda\":10.0,\"subtotal\":20.0,\"valor_iva\":2.0}]";
        try {
            JSONArray response = new JSONArray(jsonString);
            ArrayList<LinhaCarrinho> linhasCarrinhos = LinhaCarrinhoJsonParser.parserJsonLinhaCarrinho(response, getContext());
            linhaCarrinhoAdaptador = new LinhaCarrinhoAdaptador(getContext(), linhasCarrinhos);
            recyclerView.setAdapter(linhaCarrinhoAdaptador);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
        // Se você quiser usar a requisição real, descomente o código abaixo
        String url = "https://suaapi.com/carrinho"; // Substitua pela URL da sua API

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    ArrayList<LinhaCarrinho> linhasCarrinhos = LinhaCarrinhoJsonParser.parserJsonLinhaCarrinho(response, getContext());
                    linhaCarrinhoAdaptador = new LinhaCarrinhoAdaptador(getContext(), linhasCarrinhos);
                    recyclerView.setAdapter(linhaCarrinhoAdaptador);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("CarrinhoFragment", "Erro ao obter dados: " + error.toString());
                    Toast.makeText(getContext(), "Erro ao obter dados do carrinho", Toast.LENGTH_SHORT).show();
                }
            });

        requestQueue.add(jsonArrayRequest);
        */
    }
}