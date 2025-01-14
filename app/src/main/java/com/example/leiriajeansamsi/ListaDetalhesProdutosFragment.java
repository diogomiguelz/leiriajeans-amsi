package com.example.leiriajeansamsi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.leiriajeansamsi.Modelo.Carrinho;
import com.example.leiriajeansamsi.Modelo.Produto;
import com.example.leiriajeansamsi.Modelo.SingletonProdutos;

public class ListaDetalhesProdutosFragment extends Fragment {

    private TextView tvNomeProduto, tvPrecoProduto, tvDescricaoProduto, btnAdicionarCarrinho;
    private ImageView imgCapaProduto;
    private Produto produto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalhes_produtos, container, false);

        // Initialize UI components
        tvNomeProduto = view.findViewById(R.id.nomeTxt);
        tvPrecoProduto = view.findViewById(R.id.precoTxt);
        tvDescricaoProduto = view.findViewById(R.id.tvDescricaoProduto);
        imgCapaProduto = view.findViewById(R.id.pic);
        btnAdicionarCarrinho = view.findViewById(R.id.btnAdicionarCarrinho);

        // Get product from arguments
        if (getArguments() != null && getArguments().containsKey(DetalhesProdutoActivity.PRODUTO)) {
            produto = getArguments().getParcelable(DetalhesProdutoActivity.PRODUTO);

            // Populate the UI with product data
            tvNomeProduto.setText(produto.getNome());
            tvPrecoProduto.setText(produto.getPreco() + " €");
            tvDescricaoProduto.setText(produto.getDescricao());

            String imageUrl = "http://" + SingletonProdutos.getInstance(getContext()).getApiIP(getContext()) +
                    "/leiriajeans/frontend/web/public/images/produtos/" + produto.getImagem();

            Glide.with(getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ipllogo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCapaProduto);
        }

        // Add product to cart
        btnAdicionarCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Carrinho carrinho = SingletonProdutos.getInstance(getContext()).getCarrinho();
                if (carrinho == null) {
                    SingletonProdutos.getInstance(getContext()).getCarrinhoAPI(getContext());
                    carrinho = SingletonProdutos.getInstance(getContext()).getCarrinho();
                } else {
                    SingletonProdutos.getInstance(getContext()).adicionarLinhaCarrinhoAPI(getContext(), produto, carrinho);
                    Toast.makeText(getContext(), "Produto adicionado ao carrinho", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
