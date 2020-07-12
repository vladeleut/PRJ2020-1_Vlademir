package com.eleut.gerenciadorlanchonete.Classes;

public class Produto {
    private String keyProduto;
    private String nomeProduto;
    private String descProduto;
    private String urlImgProduto;
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

    public String getUrlImgProduto() {
        return urlImgProduto;
    }

    public void setUrlImgProduto(String urlImgProduto) {
        this.urlImgProduto = urlImgProduto;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }
}
