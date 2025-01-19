package com.example.leiriajeansamsi;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leiriajeansamsi.Modelo.Produto;
import com.example.leiriajeansamsi.Modelo.Singleton;
import com.example.leiriajeansamsi.adaptadores.ListaProdutosAdaptador;
import com.example.leiriajeansamsi.listeners.ProdutoListener;
import com.example.leiriajeansamsi.listeners.ProdutosListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class ListaProdutosFragment extends Fragment implements ProdutosListener, ProdutoListener {

    private TextView tvNomeProduto, tvPrecoProduto;
    public ArrayList<Produto> listaProdutos;
    private RecyclerView rvProdutos;
    private ListaProdutosAdaptador adapter;
    private ProgressBar progressBar;
    private SearchView searchView;
    private ArrayList<Produto> produtosOriginais;
    private boolean ordenacaoCrescente = true;
    private Spinner spinnerCategoria;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_produtos, container, false);

        // Set the title for the fragment
        getActivity().setTitle("Produtos");

        spinnerCategoria = view.findViewById(R.id.spinnerCategoria); // Adicione junto às outras inicializações

        // Initialize UI components
        rvProdutos = view.findViewById(R.id.listProdutos);
        progressBar = view.findViewById(R.id.progressBarProdutos);

        // Configure RecyclerView
        rvProdutos.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Set up Singleton listener
        Singleton.getInstance(getContext()).setProdutosListener(this);
        Singleton.getInstance(getContext()).getAllProdutosAPI(getContext());

        // Inicializar SearchView
        searchView = view.findViewById(R.id.searchView);
        setupSearchView();

        return view;
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrarProdutos(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarProdutos(newText);
                return true;
            }
        });
    }

    private void setupSpinnerCategoria() {
        ArrayList<String> categorias = new ArrayList<>();
        categorias.add("Todas");

        // Adicionar categorias únicas dos produtos
        if (produtosOriginais != null) {
            Set<String> categoriasUnicas = new HashSet<>();
            for (Produto produto : produtosOriginais) {
                if (produto.getCategoria() != null && !produto.getCategoria().isEmpty()) {
                    categoriasUnicas.add(produto.getCategoria());
                }
            }
            categorias.addAll(categoriasUnicas);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                categorias
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoriaSelecionada = categorias.get(position);
                if (categoriaSelecionada.equals("Todas")) {
                    filtrarPorCategoria(null);
                } else {
                    filtrarPorCategoria(categoriaSelecionada);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filtrarPorCategoria(null);
            }
        });
    }

    private void filtrarPorCategoria(String categoria) {
        if (produtosOriginais != null) {
            ArrayList<Produto> produtosFiltrados;

            if (categoria == null) {
                produtosFiltrados = new ArrayList<>(produtosOriginais);
            } else {
                produtosFiltrados = new ArrayList<>();
                for (Produto produto : produtosOriginais) {
                    if (produto.getCategoria().equalsIgnoreCase(categoria)) {
                        produtosFiltrados.add(produto);
                    }
                }
            }

            adapter = new ListaProdutosAdaptador(this, getContext(), produtosFiltrados);
            rvProdutos.setAdapter(adapter);
        }
    }

    private void filtrarProdutos(String query) {
        if (produtosOriginais != null) {
            ArrayList<Produto> produtosFiltrados = new ArrayList<>();

            for (Produto produto : produtosOriginais) {
                if (produto.getNome().toLowerCase().contains(query.toLowerCase())) {
                    produtosFiltrados.add(produto);
                }
            }

            adapter = new ListaProdutosAdaptador(this, getContext(), produtosFiltrados);
            rvProdutos.setAdapter(adapter);
        }
    }

    @Override
    public void onRefreshListaProdutos(ArrayList<Produto> listaProdutos) {
        this.produtosOriginais = new ArrayList<>(listaProdutos);
        setupSpinnerCategoria();

        // Aplicar ordenação atual aos produtos
        ArrayList<Produto> produtosOrdenados = new ArrayList<>(listaProdutos);
        Collections.sort(produtosOrdenados, new Comparator<Produto>() {
            @Override
            public int compare(Produto p1, Produto p2) {
                if (ordenacaoCrescente) {
                    return Float.compare(p1.getPreco(), p2.getPreco());
                } else {
                    return Float.compare(p2.getPreco(), p1.getPreco());
                }
            }
        });

        // Update RecyclerView adapter
        adapter = new ListaProdutosAdaptador(this, getContext(), produtosOrdenados);
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
            args.putParcelable(DetalhesProdutosFragment.PRODUTO, product);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Habilita o menu de opções
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_produtos, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sort) {
            ordenacaoCrescente = !ordenacaoCrescente;
            ordenarProdutos();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ordenarProdutos() {
        if (produtosOriginais != null) {
            ArrayList<Produto> produtosOrdenados = new ArrayList<>(produtosOriginais);

            Collections.sort(produtosOrdenados, new Comparator<Produto>() {
                @Override
                public int compare(Produto p1, Produto p2) {
                    if (ordenacaoCrescente) {
                        return Float.compare(p1.getPreco(), p2.getPreco());
                    } else {
                        return Float.compare(p2.getPreco(), p1.getPreco());
                    }
                }
            });

            adapter = new ListaProdutosAdaptador(this, getContext(), produtosOrdenados);
            rvProdutos.setAdapter(adapter);

            // Mostrar mensagem de ordenação
            String mensagem = ordenacaoCrescente ? "Preço: Menor para Maior" : "Preço: Maior para Menor";
            Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
        }
    }

}
