package com.example.leiriajeansamsi.adaptadores;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.leiriajeansamsi.DetalhesProdutosFragment;
import com.example.leiriajeansamsi.Modelo.Produto;
import com.example.leiriajeansamsi.Modelo.Singleton;
import com.example.leiriajeansamsi.R;
import com.example.leiriajeansamsi.listeners.ProdutoListener;

import java.util.ArrayList;

public class ListaProdutosAdaptador extends RecyclerView.Adapter<ListaProdutosAdaptador.ViewHolder> implements ProdutoListener {
    private ProdutoListener produtoListener;
    public Context context;
    private ArrayList<Produto> produtos;

    public ListaProdutosAdaptador(ProdutoListener produtoListener, Context context, ArrayList<Produto> produtos) {
        this.produtoListener = produtoListener;
        this.context = context;
        this.produtos = produtos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View inflate = layoutInflater.inflate(R.layout.item_lista_produtos, parent, false); // Corrigido para usar 'parent' ao invés de 'null'
        return new ViewHolder(inflate, produtoListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Produto product = produtos.get(position);
        holder.tvNomeProduto.setText(product.getNome());
        holder.tvPrecoProduto.setText(String.format("%.2f €", product.getPreco()));
        holder.tvCategoria.setText(product.getCategoria());
        String imageUrl = "http://" + Singleton.getInstance(context).getApiIP(context)
                + product.getImagem();

        imageUrl = imageUrl.replace("images/produtos/", "public/imagens/produtos/");

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.imgProduto);

        holder.itemView.setOnClickListener(view -> {
            if (produtoListener != null) {
                produtoListener.onItemClick(holder.getAdapterPosition(), product);
            }
        });

        holder.btnAddToCart.setOnClickListener(v -> {
            Singleton singleton = Singleton.getInstance(context);

            // Primeiro verifica se já existe um carrinho
            singleton.getCarrinhoAPI(context);

            // O metodo getCarrinhoAPI já inclui a lógica de criar o carrinho se não existir
            // e depois adicionar o produto como uma linha do carrinho
            singleton.criarLinhaCarrinhoAPI(context, product, 1);
            Toast.makeText(context, "Produto adicionado ao carrinho", Toast.LENGTH_SHORT).show();
        });


    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public void setProdutos(ArrayList<Produto> listaProdutos) {
        this.produtos = listaProdutos;
    }

    @Override
    public void onItemClick(int position, Produto product) {
        // Cria uma intent para abrir a DetalhesProdutoActivity
        Intent intent = new Intent(context, DetalhesProdutosFragment.class);
        // Passa o produto selecionado para a próxima activity
        intent.putExtra(DetalhesProdutosFragment.PRODUTO, product);
        // Inicia a activity
        startActivity(context, intent, null);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeProduto;
        TextView tvPrecoProduto;
        TextView tvCategoria;
        ImageView imgProduto;
        Button btnAddToCart;

        public ViewHolder(@NonNull View itemView, ProdutoListener produtoListener) {
            super(itemView);
            tvNomeProduto = itemView.findViewById(R.id.nomeTxt);
            tvPrecoProduto = itemView.findViewById(R.id.precoTxt);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            imgProduto = itemView.findViewById(R.id.pic);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }

}