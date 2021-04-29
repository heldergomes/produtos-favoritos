package com.api.produtosfavoritos.produto;

import com.api.produtosfavoritos.exception.EntidadeNaoEncontradaException;

public enum StatusProduto {
    FAVORITO("favorito");
    private String status;

    StatusProduto(String status) {
        this.status = status;
    }

    public static boolean validaStatus(String status){
        for (StatusProduto statusProduto : StatusProduto.values()){
            if (statusProduto.getStatus().equals(status))
                return true;
        }
        throw new EntidadeNaoEncontradaException("Status Invalido: " + status);
    }

    public String getStatus() {
        return status;
    }
}
