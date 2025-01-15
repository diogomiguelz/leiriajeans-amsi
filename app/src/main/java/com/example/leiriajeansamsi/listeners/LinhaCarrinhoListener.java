package com.example.leiriajeansamsi.listeners;

import com.example.leiriajeansamsi.Modelo.LinhaCarrinho;
import java.util.List;

public interface LinhaCarrinhoListener {
    /**
     * Metodo chamado para notificar que um único item foi atualizado.
     */
    void onItemUpdate();

    /**
     * Metodo chamado para atualizar toda a lista de linhas do carrinho.
     *
     * @param linhas Lista atualizada de linhas do carrinho.
     */
    void atualizarCarrinho(List<LinhaCarrinho> linhas);
}
