package com.example.leiriajeansamsi.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.books.Modelo.Fatura;
import pt.ipleiria.estg.dei.books.R;
import pt.ipleiria.estg.dei.books.listeners.FaturaListener;

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
        Fatura fatura = faturas.get(position);
        holder.tvNomeFatura.setText("Fatura: "+fatura.getId());
        holder.tvDataFatura.setText(fatura.getData());
        holder.tvValorTotalFatura.setText(fatura.getValorTotal() + " €");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(faturaListener != null){
                    faturaListener.onItemClick(position, fatura);


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
