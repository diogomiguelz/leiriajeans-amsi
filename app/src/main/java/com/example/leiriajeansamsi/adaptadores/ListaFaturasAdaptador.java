package com.example.leiriajeansamsi.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leiriajeansamsi.Modelo.Fatura;
import com.example.leiriajeansamsi.R;
import com.example.leiriajeansamsi.listeners.FaturaListener;

import java.util.ArrayList;

public class ListaFaturasAdaptador extends RecyclerView.Adapter<ListaFaturasAdaptador.ViewHolder> {

    private final FaturaListener faturaListener;
    private final Context context;
    private ArrayList<Fatura> faturas;

    public ListaFaturasAdaptador(FaturaListener faturaListener, Context context, ArrayList<Fatura> faturas) {
        this.faturaListener = faturaListener;
        this.context = context;
        this.faturas = faturas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_fatura, parent, false);
        return new ViewHolder(view, faturaListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Fatura fatura = faturas.get(position);

        // Define os valores dos TextViews com os dados do modelo Fatura
        holder.tvDataFatura.setText("Data: " + fatura.getData());
        holder.tvEstadoFatura.setText("Estado: " + fatura.getStatusPedido());
        holder.tvTotalFatura.setText("Total: " + String.format("%.2f €", fatura.getValorTotal()));

        // Configura o clique no item
        holder.itemView.setOnClickListener(view -> {
            if (faturaListener != null) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    faturaListener.onItemClick(currentPosition, fatura);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return faturas != null ? faturas.size() : 0;
    }

    public void updateFaturas(ArrayList<Fatura> novasFaturas) {
        this.faturas = novasFaturas != null ? novasFaturas : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDataFatura, tvEstadoFatura, tvTotalFatura;

        public ViewHolder(@NonNull View itemView, FaturaListener faturaListener) {
            super(itemView);
            tvDataFatura = itemView.findViewById(R.id.tvDataFatura);
            tvEstadoFatura = itemView.findViewById(R.id.tvEstadoFatura);
            tvTotalFatura = itemView.findViewById(R.id.tvTotalFatura);
        }
    }
}
