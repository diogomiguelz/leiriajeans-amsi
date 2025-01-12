package com.example.leiriajeansamsi.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leiriajeansamsi.Modelo.Fatura;

import java.util.ArrayList;

import com.example.leiriajeansamsi.Modelo.Fatura;
import com.example.leiriajeansamsi.R;
import com.example.leiriajeansamsi.listeners.FaturaListener;

public class ListaFaturasAdaptador extends RecyclerView.Adapter<ListaFaturasAdaptador.ViewHolder>{

    FaturaListener faturaListener;
    public Context context;
    private ArrayList<Fatura> faturas;
    //setup me the adapter
    public ListaFaturasAdaptador(FaturaListener faturaListener, Context context, ArrayList<Fatura> faturas) {
        this.faturaListener = faturaListener;
        this.context = context;
        this.faturas = faturas;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View inflate = layoutInflater.inflate(R.layout.item_fatura, null);
        ViewHolder viewHolder = new ViewHolder(inflate, faturaListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Obtém a fatura na posição atual
        Fatura fatura = faturas.get(position);

        // Define os dados no ViewHolder
        holder.tvNomeFatura.setText("Fatura: " + fatura.getId());
        holder.tvDataFatura.setText(fatura.getData());
        holder.tvValorTotalFatura.setText(fatura.getValorTotal() + " €");

        // Configura o clique no item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (faturaListener != null) {
                    // Usa holder.getAdapterPosition() para garantir a posição atual
                    int currentPosition = holder.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        faturaListener.onItemClick(currentPosition, fatura);
                    }
                }
            }
        });
    }




    @Override
    public int getItemCount() {
        return faturas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeFatura, tvDataFatura, tvValorTotalFatura;


        public ViewHolder(@NonNull View itemView, FaturaListener faturaListener) {
            super(itemView);
            tvNomeFatura = itemView.findViewById(R.id.tvNomeFatura);
            tvDataFatura = itemView.findViewById(R.id.tvDataFatura);
            tvValorTotalFatura = itemView.findViewById(R.id.tvValorFatura);
        }


    }
}
