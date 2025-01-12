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

import pt.ipleiria.estg.dei.books.Modelo.Carrinho;
import pt.ipleiria.estg.dei.books.Modelo.Favoritos;
import pt.ipleiria.estg.dei.books.Modelo.FavoritosBDHelper;
import pt.ipleiria.estg.dei.books.Modelo.Produto;
import pt.ipleiria.estg.dei.books.Modelo.SingletonProdutos;
import pt.ipleiria.estg.dei.books.listeners.CarrinhoListener;


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
                    .placeholder(R.drawable.logoipl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCapaProduto);
            btnAdicionarCarrinho = findViewById(R.id.btnAdicionarCarrinho);
            btnAdicionarCarrinho.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Carrinho carrinho = SingletonProdutos.getInstance(getApplicationContext()).getCarrinho();
                    if (carrinho == null) {
                        // SingletonProdutos.getInstance(getApplicationContext()).adicionarCarrinhoAPI(getApplicationContext());
                        SingletonProdutos.getInstance(getApplicationContext()).getCarrinhoAPI(getApplicationContext());
                        carrinho = SingletonProdutos.getInstance(getApplicationContext()).getCarrinho();
                    } else {
                        SingletonProdutos.getInstance(getApplicationContext()).adicionarLinhaCarrinhoAPI(getApplicationContext(), produto, carrinho);
                        Toast.makeText(getApplicationContext(), "Produto adicionado ao carrinho", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        favbtn = findViewById(R.id.favBtn);
        favbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavoriteState();
                updateFavoriteButton();
            }
        });

        // Verifica se o produto está nos favoritos e atualiza a imagem do coração
        checkAndUpdateFavoriteState();
    }

    private void toggleFavoriteState() {

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(PRODUTO)) {
            Produto produto = intent.getParcelableExtra(PRODUTO);

            isFavorite = !isFavorite;
            FavoritosBDHelper dbHelper = new FavoritosBDHelper(getApplicationContext());

            if (isFavorite) {
                assert produto != null;
                dbHelper.adicionarFavoritosBD(new Favoritos(userID, produto.getId(), produto.getNome(), produto.getCategoria(), produto.getImagem(), produto.getPreco()));
                Toast.makeText(getApplicationContext(), "Produto adicionado aos favoritos", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.removerEstadoFavoritosBD(userID, produtoID);
                Toast.makeText(getApplicationContext(), "Produto removido dos favoritos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateFavoriteButton() {
        int imageResource = isFavorite ? R.drawable.favorite_fill : R.drawable.favorite_unfilled;
        favbtn.setImageResource(imageResource);
    }

    private void checkAndUpdateFavoriteState() {
        // Receive the product id
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(PRODUTO)) {
            Produto produto = intent.getParcelableExtra(PRODUTO);
            produtoID = (produto != null) ? produto.getId() : 0;
        }

        // recebe o id do user
        userID = SingletonProdutos.getInstance(getApplicationContext()).getUserId(getApplicationContext());

        // verfica se o produto está nos favoritos para o utilizador atual
        FavoritosBDHelper dbHelper = new FavoritosBDHelper(getApplicationContext());
        isFavorite = dbHelper.isProdutoInFavorites(userID, produtoID);
        dbHelper.close();

        // Update the favorite button image
        updateFavoriteButton();
    }

    @Override
    public void onRefreshListaCarrinho(Carrinho carrinho) {
        carrinho = SingletonProdutos.getInstance(getApplicationContext()).getCarrinho();
    }
}









