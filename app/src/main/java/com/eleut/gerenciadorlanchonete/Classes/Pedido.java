package com.eleut.gerenciadorlanchonete.Classes;

public class Pedido {
    private String keyPedido;
    private String situacaoPedido;
    private String nomeCliPedido;
    private String descPedido;
    private String aberturaPedido;

    public String getKeyPedido() {
        return keyPedido;
    }

    public void setKeyPedido(String keyPedido) {
        this.keyPedido = keyPedido;
    }

    public String getSituacaoPedido() {
        return situacaoPedido;
    }

    public void setSituacaoPedido(String situacaoPedido) {
        this.situacaoPedido = situacaoPedido;
    }

    public String getNomeCliPedido() {
        return nomeCliPedido;
    }

    public void setNomeCliPedido(String nomeCliPedido) {
        this.nomeCliPedido = nomeCliPedido;
    }

    public String getDescPedido() {
        return descPedido;
    }

    public void setDescPedido(String descPedido) {
        this.descPedido = descPedido;
    }

    public String getAberturaPedido() {
        return aberturaPedido;
    }

    public void setAberturaPedido(String aberturaPedido) {
        this.aberturaPedido = aberturaPedido;
    }
}
