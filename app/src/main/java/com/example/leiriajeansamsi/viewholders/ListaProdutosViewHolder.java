package com.example.leiriajeansamsi.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.leiriajeansamsi.Modelo.Produto;
import com.example.leiriajeansamsi.R;


public class ListaProdutosViewHolder extends RecyclerView.ViewHolder {
    public TextView tvNomeProduto, tvPrecoProduto;
    //  private ImageView imgCapa;

    public ListaProdutosViewHolder(View view) {
        super(view);
        tvNomeProduto = view.findViewById(R.id.nomeTxt);
        tvPrecoProduto = view.findViewById(R.id.precoTxt);
        //imgCapa = view.findViewById(R.id.imgCapa);
    }

    public void update(Produto produto) {
        tvNomeProduto.setText(produto.getNome());
        tvPrecoProduto.setText(produto.getPreco() + " €");

        //imgCapa.setImageResource(livro.getCapa());
           /* Glide.with(context)
                    .load(produto.getCapa())
                    .placeholder(R.drawable.logoipl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCapa);
        }*/
    }


}
