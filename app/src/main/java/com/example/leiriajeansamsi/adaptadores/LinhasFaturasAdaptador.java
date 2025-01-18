package com.example.leiriajeansamsi.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leiriajeansamsi.Modelo.LinhaFatura;
import com.example.leiriajeansamsi.Modelo.Produto;
import com.example.leiriajeansamsi.Modelo.SingletonProdutos;
import com.example.leiriajeansamsi.R;

import java.util.List;
import java.util.Locale;

public class LinhasFaturasAdaptador extends RecyclerView.Adapter<LinhasFaturasAdaptador.LinhaFaturaViewHolder> {

    private final List<LinhaFatura> linhasFaturas;
    private final Context context;
    TextView tvData, tvId, tvUserId, tvValorTotal, tvStatus;



    public LinhasFaturasAdaptador(Context context, List<LinhaFatura> linhasFaturas) {
        this.context = context;
        this.linhasFaturas = linhasFaturas;
    }

    @NonNull
    @Override
    public LinhaFaturaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_linha_fatura, parent, false);
        return new LinhaFaturaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LinhaFaturaViewHolder holder, int position) {
        LinhaFatura linhaFatura = linhasFaturas.get(position);

        // Obter o produto através do SingletonProdutos
        Produto produto = SingletonProdutos.getInstance(context).getProdutoById(linhaFatura.getProdutoId());

        // Formatação dos valores monetários
        float precoUnitario = linhaFatura.getPrecoVenda();
        float subtotal = linhaFatura.getSubTotal();
        float valorIva = linhaFatura.getValorIva();

        // Configuração dos textos
        // Usar o nome do produto se disponível, caso contrário usar o ID
        String nomeProduto = produto != null ? produto.getNome() : String.format("Produto #%d", linhaFatura.getProdutoId());
        holder.tvNomeProduto.setText(nomeProduto);

        holder.tvQuantidade.setText(String.format("Quantidade: %d", linhaFatura.getQuantidade()));
        holder.tvPreco.setText(String.format(Locale.getDefault(),
                "Preço unitário: %.2f €\nIVA: %.2f €\nSubtotal: %.2f €",
                precoUnitario, valorIva, subtotal));
    }


    @Override
    public int getItemCount() {
        return linhasFaturas != null ? linhasFaturas.size() : 0;
    }

    public void updateLinhasFaturas(List<LinhaFatura> novasLinhas) {
        this.linhasFaturas.clear();
        if (novasLinhas != null) {
            this.linhasFaturas.addAll(novasLinhas);
        }
        notifyDataSetChanged();
    }

    public static class LinhaFaturaViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNomeProduto;
        final TextView tvQuantidade;
        final TextView tvPreco;

        public LinhaFaturaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomeProduto = itemView.findViewById(R.id.tvNomeProduto);
            tvQuantidade = itemView.findViewById(R.id.tvQuantidade);
            tvPreco = itemView.findViewById(R.id.tvPreco);
        }
    }

    public void clear() {
        linhasFaturas.clear();
        notifyDataSetChanged();
    }
}
