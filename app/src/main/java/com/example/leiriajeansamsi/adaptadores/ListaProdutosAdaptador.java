package com.example.leiriajeansamsi.adaptadores;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leiriajeansamsi.Modelo.Produto;
import com.example.leiriajeansamsi.Modelo.SingletonProdutos;
import com.example.leiriajeansamsi.adaptadores.ListaProdutosAdaptador;
import com.example.leiriajeansamsi.listeners.ProdutoListener;
import com.example.leiriajeansamsi.listeners.ProdutosListener;

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
            View inflate = layoutInflater.inflate(R.layout.item_lista_produtos, null);
            ViewHolder viewHolder = new ViewHolder(inflate, produtoListener);

            return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Produto product = produtos.get(position);
        holder.tvNomeProduto.setText(product.getNome());
        holder.tvPrecoProduto.setText(product.getPreco() + " €");
        String imageUrl = "http://"+ SingletonProdutos.getInstance(context).getApiIP(context) +"/leiriajeans/frontend/web/public/imagens/produtos/" + product.getImagem();

        Glide.with(holder.itemView.getContext()).load(imageUrl).transform(new CenterCrop(), new RoundedCorners(30)).into(holder.imgProduto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use holder.getAdapterPosition() instead of position
                if (produtoListener != null) {
                    produtoListener.onItemClick(holder.getAdapterPosition(), product);
                }
            }
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
        Intent intent = new Intent(context, DetalhesProdutoActivity.class);

        // Passa o produto selecionado para a próxima activity
        intent.putExtra(DetalhesProdutoActivity.PRODUTO, product);

        // Inicia a activity
        startActivity(context, intent, null);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeProduto;
        TextView tvPrecoProduto;
        ImageView imgProduto;

        public ViewHolder(@NonNull View itemView, ProdutoListener produtoListener) {
            super(itemView);
            tvNomeProduto = itemView.findViewById(R.id.nomeTxt);
            tvPrecoProduto = itemView.findViewById(R.id.precoTxt);
            imgProduto = itemView.findViewById(R.id.pic);

        }
    }
}

