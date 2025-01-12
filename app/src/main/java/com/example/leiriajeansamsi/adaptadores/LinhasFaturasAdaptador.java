package com.example.leiriajeansamsi.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.leiriajeansamsi.Modelo.LinhaFatura;

import java.util.List;

import com.example.leiriajeansamsi.Modelo.LinhaFatura;
import com.example.leiriajeansamsi.R;

public class LinhasFaturasAdaptador extends RecyclerView.Adapter<LinhasFaturasAdaptador.ViewHolder> {

    private List<LinhaFatura> linhaFatura;
    private Context context;

    public LinhasFaturasAdaptador(Context context, List<LinhaFatura> linhaFatura) {
        this.context = context;
        this.linhaFatura = linhaFatura;
    }

    public void setLinhaFatura(List<LinhaFatura> linhaFatura) {
        this.linhaFatura = linhaFatura;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_linha_fatura, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LinhaFatura linha = linhaFatura.get(position);

        holder.textViewNome.setText(linha.getProduto());
        holder.textViewPreco.setText(linha.getPrecoVenda()+linha.getValorIva() + " €");
        holder.textViewPercentagemIva.setText(linha.getIva() + " %");
        holder.textViewQuantidade.setText(String.valueOf(linha.getQuantidade()));
        holder.textViewTotal.setText(linha.getSubTotal() + " €");
    }

    @Override
    public int getItemCount() {
        return linhaFatura.size();
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
