package com.example.leiriajeansamsi.adaptadores;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.books.MainActivity;
import pt.ipleiria.estg.dei.books.Modelo.Carrinho;
import pt.ipleiria.estg.dei.books.Modelo.LinhaCarrinho;
import pt.ipleiria.estg.dei.books.Modelo.Produto;
import pt.ipleiria.estg.dei.books.Modelo.SingletonProdutos;
import pt.ipleiria.estg.dei.books.R;
import pt.ipleiria.estg.dei.books.listeners.LinhaCarrinhoListener;
import pt.ipleiria.estg.dei.books.listeners.LinhasCarrinhosListener;

public class LinhaCarrinhoAdaptador extends RecyclerView.Adapter<LinhaCarrinhoAdaptador.ViewHolder> {

    public Context context;
    private LinhasCarrinhosListener linhasCarrinhosListener;
    private LinhaCarrinhoListener linhaCarrinhoListener;
    private ArrayList<LinhaCarrinho> linhasCarrinho;
    private Carrinho carrinho;
    private MainActivity mainActivity;


    public LinhaCarrinhoAdaptador(Context context, LinhasCarrinhosListener linhasCarrinhosListener, ArrayList<LinhaCarrinho> linhasCarrinho, LinhaCarrinhoListener linhaCarrinhoListener,Carrinho carrinho) {
        this.context = context;
        this.linhasCarrinhosListener = linhasCarrinhosListener;
        this.linhasCarrinho = linhasCarrinho;
        this.linhaCarrinhoListener = linhaCarrinhoListener;
        this.carrinho = carrinho;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrinho, parent, false);
        return new ViewHolder(view, linhasCarrinhosListener);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        LinhaCarrinho linhaCarrinho = linhasCarrinho.get(position);
        int idpro = linhaCarrinho.getProdutoID();
        Produto produto = SingletonProdutos.getInstance(context).getProduto(idpro);
        if(produto!=null){
            String nomeproduto = produto.getNome();
            holder.tvNomeProdutoCarrinho.setText(produto.getNome());
            holder.tvPrecoProdutoCarrinho.setText(produto.getPreco() + " € - "+String.format("%.2f",(produto.getPreco()*linhaCarrinho.getQuantidade()))+" €");
            holder.tvQuantidadeProdutoCarrinho.setText(linhaCarrinho.getQuantidade() + "");
            String imageUrl = "http://"+ SingletonProdutos.getInstance(context).getApiIP(context) +"/AMAI-plataformas/frontend/web/public/imagens/produtos/" + produto.getImagem();
            Glide.with(holder.itemView.getContext()).load(imageUrl).transform(new CenterCrop(), new RoundedCorners(30)).into(holder.imgProdutoCarrinho);
        }




        //holder.tvTotalCarrinho.setText("0.00 €");

        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increase quantity
                linhaCarrinho.adicionarQuantidade();

                SingletonProdutos.getInstance(context).updateLinhaCarrinhoAPI(context,linhaCarrinho);
                SingletonProdutos.getInstance(context).setLinhaCarrinhoListener(linhaCarrinhoListener);
                SingletonProdutos.getInstance(context).getCarrinhoAPI(context);

                notifyItemChanged(position);
            }
        });

        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Decrease quantity
                linhaCarrinho.diminuirQuantidade();

                SingletonProdutos.getInstance(context).updateLinhaCarrinhoAPI(context,linhaCarrinho);
                SingletonProdutos.getInstance(context).setLinhaCarrinhoListener(linhaCarrinhoListener);
                SingletonProdutos.getInstance(context).getCarrinhoAPI(context);
                notifyItemChanged(position);

            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context buttonContext = v.getContext();
                // Delete item
                AlertDialog alert = new AlertDialog.Builder(buttonContext)
                        .setTitle("Remover produto")
                        .setMessage("Tem a certeza que pretende remover o produto do carrinho?")
                        .setPositiveButton("Sim", (dialog, which) -> {
                            SingletonProdutos.getInstance(buttonContext).deleteLinhaCarrinhoAPI(buttonContext,linhaCarrinho);
                            SingletonProdutos.getInstance(buttonContext).setLinhaCarrinhoListener(linhaCarrinhoListener);

                            linhasCarrinho.remove(position);

                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, linhasCarrinho.size());
                            SingletonProdutos.getInstance(context).getCarrinhoAPI(context);


                        })
                        .setNegativeButton("Não", null)
                        .create();
                alert.show();


            }
        });


    }

    @Override
    public int getItemCount() {
        return linhasCarrinho.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeProdutoCarrinho, tvPrecoProdutoCarrinho, tvQuantidadeProdutoCarrinho, btnIncrease, btnDecrease;
        ImageView imgProdutoCarrinho;
        Button btnDelete;


        public ViewHolder(@NonNull View itemView, LinhasCarrinhosListener linhasCarrinhosListener) {
            super(itemView);
            tvNomeProdutoCarrinho = itemView.findViewById(R.id.tvNomeProdutoCarrinho);
            tvPrecoProdutoCarrinho = itemView.findViewById(R.id.tvPrecoProdutoCarrinho);
            tvQuantidadeProdutoCarrinho = itemView.findViewById(R.id.tvQuantidadeProdutoCarrinho);
            imgProdutoCarrinho = itemView.findViewById(R.id.imgProdutoCarrinho);
            btnIncrease = itemView.findViewById(R.id.btnAumentaQtd);
            btnDecrease = itemView.findViewById(R.id.btnDiminuiQtd);
            btnDelete = itemView.findViewById(R.id.btnDelete);



        }
    }
}
