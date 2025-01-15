package com.example.leiriajeansamsi.listeners;

import com.example.leiriajeansamsi.Modelo.LinhaCarrinho;
import java.util.List;

public interface LinhaCarrinhoListener {
    void onItemUpdate();
    void atualizarCarrinho(List<LinhaCarrinho> linhas);
}
