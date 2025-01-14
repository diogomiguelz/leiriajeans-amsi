package com.example.leiriajeansamsi.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leiriajeansamsi.Modelo.LinhaCarrinho;
import com.example.leiriajeansamsi.R;

import java.util.List;

public class LinhaCarrinhoAdaptador extends RecyclerView.Adapter<LinhaCarrinhoAdaptador.LinhaCarrinhoViewHolder> {

    private Context context;
    private List<LinhaCarrinho> linhasCarrinho;

    public LinhaCarrinhoAdaptador(Context context, List<LinhaCarrinho> linhasCarrinho) {
        this.context = context;
        this.linhasCarrinho = linhasCarrinho;
    }

    @NonNull
    @Override
    public LinhaCarrinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carrinho, parent, false);
        return new LinhaCarrinhoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LinhaCarrinhoViewHolder holder, int position) {
        LinhaCarrinho linhaCarrinho = linhasCarrinho.get(position);

        // Preenche os dados no layout do item
        holder.tvNomeProdutoCarrinho.setText(linhaCarrinho.getProduto().getNome());
        holder.tvPrecoProdutoCarrinho.setText(String.format("R$ %.2f", linhaCarrinho.getProduto().getPreco()));
        holder.tvQuantidadeProdutoCarrinho.setText("Quantidade: " + linhaCarrinho.getQuantidade());

        // Carregar a imagem do produto se a imagem for local (se não, use recursos locais)
        String imagemProduto = linhaCarrinho.getProduto().getImagem();
        if (imagemProduto != null && !imagemProduto.isEmpty()) {
            // Supondo que as imagens estão em drawable (você pode ajustar conforme necessário)
            int imageRes = context.getResources().getIdentifier(imagemProduto, "drawable", context.getPackageName());
            if (imageRes != 0) {
                holder.imgProdutoCarrinho.setImageResource(imageRes);
            }
        }

        // Configurações de botões (aumentar, diminuir, deletar)
        holder.btnAumentaQtd.setOnClickListener(v -> {
            // Aumentar a quantidade
            linhaCarrinho.setQuantidade(linhaCarrinho.getQuantidade() + 1);
            notifyItemChanged(position); // Atualiza a linha
        });

        holder.btnDiminuiQtd.setOnClickListener(v -> {
            // Diminuir a quantidade
            if (linhaCarrinho.getQuantidade() > 1) {
                linhaCarrinho.setQuantidade(linhaCarrinho.getQuantidade() - 1);
                notifyItemChanged(position); // Atualiza a linha
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            // Remover o item do carrinho
            linhasCarrinho.remove(position);
            notifyItemRemoved(position); // Remove o item da lista
        });
    }

    @Override
    public int getItemCount() {
        return linhasCarrinho.size();
    }

    public void updateData(List<LinhaCarrinho> novasLinhas) {
        this.linhasCarrinho = novasLinhas;
        notifyDataSetChanged();
    }

    public static class LinhaCarrinhoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeProdutoCarrinho, tvPrecoProdutoCarrinho, tvQuantidadeProdutoCarrinho;
        ImageView imgProdutoCarrinho;
        Button btnAumentaQtd, btnDiminuiQtd, btnDelete;

        public LinhaCarrinhoViewHolder(View itemView) {
            super(itemView);
            tvNomeProdutoCarrinho = itemView.findViewById(R.id.tvNomeProdutoCarrinho);
            tvPrecoProdutoCarrinho = itemView.findViewById(R.id.tvPrecoProdutoCarrinho);
            tvQuantidadeProdutoCarrinho = itemView.findViewById(R.id.tvQuantidadeProdutoCarrinho);
            imgProdutoCarrinho = itemView.findViewById(R.id.imgProdutoCarrinho);
            btnAumentaQtd = itemView.findViewById(R.id.btnAumentaQtd);
            btnDiminuiQtd = itemView.findViewById(R.id.btnDiminuiQtd);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
