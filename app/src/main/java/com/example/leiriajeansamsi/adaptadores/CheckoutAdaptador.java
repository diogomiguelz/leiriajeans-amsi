package com.example.leiriajeansamsi.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leiriajeansamsi.Modelo.LinhaCarrinho;
import com.example.leiriajeansamsi.R;

import java.util.List;

public class CheckoutAdaptador extends RecyclerView.Adapter<CheckoutAdaptador.CheckoutViewHolder> {

    private Context context;
    private List<LinhaCarrinho> linhasCarrinho;

    public CheckoutAdaptador(Context context, List<LinhaCarrinho> linhasCarrinho) {
        this.context = context;
        this.linhasCarrinho = linhasCarrinho;
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carrinho, parent, false);
        return new CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {
        LinhaCarrinho linha = linhasCarrinho.get(position);
        holder.tvNomeProduto.setText(linha.getProduto().getNome());
        holder.tvQuantidade.setText("Quantidade: " + linha.getQuantidade());
        holder.tvPreco.setText(String.format("%.2f €", linha.getSubTotal()));
    }

    @Override
    public int getItemCount() {
        return linhasCarrinho.size();
    }

    public static class CheckoutViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeProduto, tvQuantidade, tvPreco;

        public CheckoutViewHolder(View itemView) {
            super(itemView);
            tvNomeProduto = itemView.findViewById(R.id.tvNomeProduto);
            tvQuantidade = itemView.findViewById(R.id.tvQuantidade);
            tvPreco = itemView.findViewById(R.id.tvPreco);
        }
    }
}
