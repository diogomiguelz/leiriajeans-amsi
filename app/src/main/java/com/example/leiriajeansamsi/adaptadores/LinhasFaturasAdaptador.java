package com.example.leiriajeansamsi.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.leiriajeansamsi.Modelo.LinhaFatura;

import java.util.List;

import pt.ipleiria.estg.dei.books.Modelo.LinhasFaturas;
import pt.ipleiria.estg.dei.books.R;

public class LinhasFaturasAdaptador extends RecyclerView.Adapter<LinhasFaturasAdaptador.ViewHolder> {

    private List<LinhasFaturas> linhasFaturas;
    private Context context;

    public LinhasFaturasAdaptador(Context context, List<LinhasFaturas> linhasFaturas) {
        this.context = context;
        this.linhasFaturas = linhasFaturas;
    }

    public void setLinhasFaturas(List<LinhasFaturas> linhasFaturas) {
        this.linhasFaturas = linhasFaturas;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_linhas_faturas, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LinhaFatura linha = linhasFaturas.get(position);

        holder.textViewNome.setText(linha.getNomeProduto());
        holder.textViewPreco.setText(linha.getValor()+linha.getValor_iva() + " €");
        holder.textViewPercentagemIva.setText(linha.getIva() + " %");
        holder.textViewQuantidade.setText(String.valueOf(linha.getQuantidade()));
        holder.textViewTotal.setText(linha.getTotal() + " €");
    }

    @Override
    public int getItemCount() {
        return linhasFaturas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNome, textViewPreco, textViewPrecoIva, textViewPercentagemIva, textViewQuantidade, textViewTotal;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewNome = itemView.findViewById(R.id.tvNome);
            textViewPreco = itemView.findViewById(R.id.tvPreco);
            textViewPercentagemIva = itemView.findViewById(R.id.tvPercentagemIva);
            textViewQuantidade = itemView.findViewById(R.id.tvQuantidade);
            textViewTotal = itemView.findViewById(R.id.tvTotal);
        }
    }
}
