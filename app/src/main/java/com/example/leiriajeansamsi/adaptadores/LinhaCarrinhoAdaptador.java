package com.example.leiriajeansamsi.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leiriajeansamsi.Modelo.LinhaCarrinho;
import com.example.leiriajeansamsi.Modelo.SingletonProdutos;
import com.example.leiriajeansamsi.R;
import com.example.leiriajeansamsi.listeners.LinhaCarrinhoListener;

import java.util.List;

public class LinhaCarrinhoAdaptador extends RecyclerView.Adapter<LinhaCarrinhoAdaptador.LinhaCarrinhoViewHolder> {

    private Context context;
    private List<LinhaCarrinho> linhasCarrinho;
    private LinhaCarrinhoListener linhaCarrinhoListener;

    public LinhaCarrinhoAdaptador(Context context, List<LinhaCarrinho> linhasCarrinho, LinhaCarrinhoListener linhaCarrinhoListener) {
        this.context = context;
        this.linhasCarrinho = linhasCarrinho;
        this.linhaCarrinhoListener = linhaCarrinhoListener;
    }

    @NonNull
    @Override
    public LinhaCarrinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carrinho, parent, false);
        return new LinhaCarrinhoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LinhaCarrinhoViewHolder holder, int position) {
        LinhaCarrinho linhaCarrinho = linhasCarrinho.get(position);

        holder.tvNomeProdutoCarrinho.setText(linhaCarrinho.getProduto().getNome());
        holder.tvPrecoProdutoCarrinho.setText(String.format("%.2f €", linhaCarrinho.getProduto().getPreco()));
        holder.tvQuantidadeProdutoCarrinho.setText("Quantidade: " + linhaCarrinho.getQuantidade());

        holder.btnAumentaQtd.setOnClickListener(v -> {
            SingletonProdutos.getInstance(context).aumentarQuantidade(context, linhaCarrinho, linhasAtualizadas -> {
                if (linhasAtualizadas != null) {
                    updateData(linhasAtualizadas);
                }
            });
        });

        holder.btnDiminuiQtd.setOnClickListener(v -> {
            if (linhaCarrinho.getQuantidade() > 1) {
                SingletonProdutos.getInstance(context).diminuirQuantidade(context, linhaCarrinho, linhasAtualizadas -> {
                    if (linhasAtualizadas != null) {
                        updateData(linhasAtualizadas);
                    }
                });
            } else {
                Toast.makeText(context, "Quantidade mínima é 1", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            SingletonProdutos.getInstance(context).removerLinhaCarrinhoAPI(context, linhaCarrinho.getId(), linhasAtualizadas -> {
                if (linhasAtualizadas != null) {
                    updateData(linhasAtualizadas);
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return linhasCarrinho.size();
    }

    public void updateData(List<LinhaCarrinho> novasLinhas) {
        this.linhasCarrinho.clear();
        this.linhasCarrinho.addAll(novasLinhas);
        notifyDataSetChanged();
    }

    public static class LinhaCarrinhoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeProdutoCarrinho, tvPrecoProdutoCarrinho, tvQuantidadeProdutoCarrinho;
        Button btnAumentaQtd, btnDiminuiQtd, btnDelete;

        public LinhaCarrinhoViewHolder(View itemView) {
            super(itemView);
            tvNomeProdutoCarrinho = itemView.findViewById(R.id.tvNomeProduto);
            tvPrecoProdutoCarrinho = itemView.findViewById(R.id.tvPreco);
            tvQuantidadeProdutoCarrinho = itemView.findViewById(R.id.tvQuantidade);
            btnAumentaQtd = itemView.findViewById(R.id.btnAumentaQtd);
            btnDiminuiQtd = itemView.findViewById(R.id.btnDiminuiQtd);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}