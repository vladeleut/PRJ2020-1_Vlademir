package com.eleut.gerenciadorlanchonete.Classes;

public class Produto {
    private String keyProduto;
    private String nomeProduto;
    private String descProduto;
    private String urlImagem;
    private String preco;
    private String disponibilidade;

    public String getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(String disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public String getKeyProduto() {
        return keyProduto;
    }

    public void setKeyProduto(String keyProduto) {
        this.keyProduto = keyProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getDescProduto() {
        return descProduto;
    }

    public void setDescProduto(String descProduto) {
        this.descProduto = descProduto;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getPreco() {
        return "R$"+ preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }
}
