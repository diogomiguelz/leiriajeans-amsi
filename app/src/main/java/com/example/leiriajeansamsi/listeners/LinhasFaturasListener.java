package com.example.leiriajeansamsi.listeners;

import com.example.leiriajeansamsi.Modelo.LinhaFatura;

import java.util.ArrayList;

public interface LinhasFaturasListener {
    void onRefreshListaLinhasFaturas(int faturaId, ArrayList<LinhaFatura> linhasFaturas);

    void onLinhaFaturaCriada(LinhaFatura linhaFatura);
}
