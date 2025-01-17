package com.example.leiriajeansamsi.Modelo;

import android.os.Parcel;
import android.os.Parcelable;  // Certifique-se de importar Parcelable

public class Produto implements Parcelable {

    private int id, iva, stock;
    private String nome, descricao, categoria, imagem, cor;
    private float preco;

    public Produto(int id, int iva, int stock, String nome, String descricao, String categoria, String imagem, String cor, float preco) {
        this.id = id;
        this.iva = iva;
        this.stock = stock;
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.imagem = imagem;
        this.cor = cor;
        this.preco = preco;
    }

    // Getters e setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIva() {
        return iva;
    }

    public void setIva(int iva) {
        this.iva = iva;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", iva=" + iva +
                ", nome='" + nome + '\'' +
                ", stock=" + stock +
                ", descricao='" + descricao + '\'' +
                ", cor='" + cor + '\'' +
                ", categoria='" + categoria + '\'' +
                ", imagem='" + imagem + '\'' +
                ", preco=" + preco +
                '}';
    }

    // Implementação do Parcelable
    protected Produto(Parcel in) {
        id = in.readInt();
        iva = in.readInt();
        nome = in.readString();
        stock = in.readInt();
        descricao = in.readString();
        cor = in.readString();
        categoria = in.readString();
        imagem = in.readString();
        preco = in.readFloat();
    }

    // Metodo obrigatório para escrever os dados no Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(iva);
        dest.writeString(nome);
        dest.writeInt(stock);
        dest.writeString(descricao);
        dest.writeString(cor);
        dest.writeString(categoria);
        dest.writeString(imagem);
        dest.writeFloat(preco);
    }

    // Metodo obrigatório para criar a instância a partir de um Parcel
    public static final Creator<Produto> CREATOR = new Creator<Produto>() {
        @Override
        public Produto createFromParcel(Parcel in) {
            return new Produto(in);
        }

        @Override
        public Produto[] newArray(int size) {
            return new Produto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
