package com.example.leiriajeansamsi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.leiriajeansamsi.Modelo.Produto;
import com.example.leiriajeansamsi.Modelo.Singleton;

public class DetalhesProdutosFragment extends Fragment {

    private TextView tvNomeProduto, tvPrecoProduto, tvDescricaoProduto, btnAdicionarCarrinho, tvCategoria;
    private ImageView imgCapaProduto;
    private ImageButton btnVoltar;
    private Produto produto;

    public static final String PRODUTO = "PRODUTO";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalhes_produtos, container, false);

        getActivity().setTitle("Detalhes do Produto");

        tvNomeProduto = view.findViewById(R.id.nomeTxt);
        tvPrecoProduto = view.findViewById(R.id.precoTxt);
        tvDescricaoProduto = view.findViewById(R.id.tvDescricaoProduto);
        imgCapaProduto = view.findViewById(R.id.pic);
        btnAdicionarCarrinho = view.findViewById(R.id.btnAdicionarCarrinho);
        btnVoltar = view.findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        tvCategoria = view.findViewById(R.id.tvCategoria);

        if (getArguments() != null && getArguments().containsKey(PRODUTO)) {
            produto = getArguments().getParcelable(PRODUTO);

            tvNomeProduto.setText(produto.getNome());
            tvPrecoProduto.setText(produto.getPreco() + " €");
            tvDescricaoProduto.setText(produto.getDescricao());
            tvCategoria.setText("Categoria: " + produto.getCategoria());

            String imageUrl = "http://" + Singleton.getInstance(getContext()).getApiIP(getContext())
                    + produto.getImagem();

            imageUrl = imageUrl.replace("images/produtos/", "public/imagens/produtos/");

            Glide.with(getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ipllogo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.logo2)
                    .into(imgCapaProduto);
        }

        btnAdicionarCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton singleton = Singleton.getInstance(getContext());

                // Verifica/cria o carrinho primeiro
                singleton.getCarrinhoAPI(getContext());

                // Adiciona o produto ao carrinho
                singleton.criarLinhaCarrinhoAPI(getContext(), produto, 1);
                Toast.makeText(getContext(), "Produto adicionado ao carrinho", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}