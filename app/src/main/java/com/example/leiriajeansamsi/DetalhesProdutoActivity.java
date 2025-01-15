package com.example.leiriajeansamsi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.leiriajeansamsi.Modelo.Carrinho;
import com.example.leiriajeansamsi.Modelo.Produto;
import com.example.leiriajeansamsi.Modelo.SingletonProdutos;
import com.example.leiriajeansamsi.listeners.CarrinhoListener;

public class DetalhesProdutoActivity extends AppCompatActivity implements CarrinhoListener {


    private TextView tvNomeProduto, tvPrecoProduto, tvDescricaoProduto, btnAdicionarCarrinho;
    private ImageView imgCapaProduto;
    public static final String PRODUTO = "PRODUTO";
    private CarrinhoListener carrinhoListener;

    // Favoritos
    public static final String IS_FAVORITE = "is_favorite";
    private boolean isFavorite = false;
    private ImageView favbtn;
    private int produtoID;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produto);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(PRODUTO)) {
            Produto produto = intent.getParcelableExtra(PRODUTO);

            tvNomeProduto = findViewById(R.id.nomeTxt);
            tvNomeProduto.setText(produto.getNome());
            tvPrecoProduto = findViewById(R.id.precoTxt);
            tvPrecoProduto.setText(produto.getPreco() + " €");
            tvDescricaoProduto = findViewById(R.id.tvDescricaoProduto);
            tvDescricaoProduto.setText(produto.getDescricao());
            imgCapaProduto = findViewById(R.id.pic);
            String imageUrl = "http://"+ SingletonProdutos.getInstance(getApplicationContext()).getApiIP(getApplicationContext()) +"/AMAI-plataformas/frontend/web/public/imagens/produtos/" + produto.getImagem();

            Glide.with(getApplicationContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ipllogo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCapaProduto);
            btnAdicionarCarrinho = findViewById(R.id.btnAdicionarCarrinho);
            btnAdicionarCarrinho.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SingletonProdutos.getInstance(getApplicationContext())
                        .adicionarLinhaCarrinhoAPI(getApplicationContext(), produto, 1);
                }
            });
        }

    }


    public void onRefreshListaCarrinho(Carrinho carrinho) {
        carrinho = SingletonProdutos.getInstance(getApplicationContext()).getCarrinho();
    }
}









