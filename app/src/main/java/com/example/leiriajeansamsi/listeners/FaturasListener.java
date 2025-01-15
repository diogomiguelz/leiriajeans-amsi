package com.example.leiriajeansamsi.listeners;

import java.util.ArrayList;

import com.example.leiriajeansamsi.Modelo.Fatura;

public interface FaturasListener {
    void onRefreshListaFatura(ArrayList<Fatura> faturas);
    void onFaturaCriada(Fatura fatura);
}
