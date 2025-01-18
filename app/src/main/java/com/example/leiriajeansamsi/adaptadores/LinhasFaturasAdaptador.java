package com.example.leiriajeansamsi.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leiriajeansamsi.Modelo.LinhaFatura;
import com.example.leiriajeansamsi.Modelo.MetodoExpedicao;
import com.example.leiriajeansamsi.Modelo.MetodoPagamento;
import com.example.leiriajeansamsi.Modelo.Produto;
import com.example.leiriajeansamsi.Modelo.SingletonProdutos;
import com.example.leiriajeansamsi.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LinhasFaturasAdaptador extends RecyclerView.Adapter<LinhasFaturasAdaptador.LinhaFaturaViewHolder> {

    private final List<LinhaFatura> linhasFaturas;
    private final Context context;
    TextView tvData, tvId, tvUserId, tvValorTotal, tvStatus;
    private List<MetodoPagamento> metodosPagamento = new ArrayList<>();
    private List<MetodoExpedicao> metodosExpedicao = new ArrayList<>();
    private Spinner spinnerMetodoPagamento, spinnerMetodoExpedicao;

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
        Produto produto = SingletonProdutos.getInstance(context).getProdutoById(linhaFatura.getProdutoId());

        // Formatação dos valores monetários
        float precoUnitario = linhaFatura.getPrecoVenda();
        float valorIva = precoUnitario * (produto != null ? produto.getIva() / 100f : 0) * linhaFatura.getQuantidade();
        float subtotal = (precoUnitario * linhaFatura.getQuantidade()) + valorIva;

        // Configuração dos textos
        if (produto != null) {
            holder.tvNomeProduto.setText(produto.getNome());
        } else {
            holder.tvNomeProduto.setText("Produto não encontrado");
        }

        holder.tvQuantidade.setText(String.format("Quantidade: %d", linhaFatura.getQuantidade()));
        holder.tvPreco.setText(String.format(Locale.getDefault(),
                "Preço unitário: %.2f €\n" +
                        "IVA: %.2f €\n" +
                        "Subtotal: %.2f €",
                precoUnitario,
                valorIva,
                subtotal));
    }

    public List<LinhaFatura> getLinhasFaturas() {
        return linhasFaturas;
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
