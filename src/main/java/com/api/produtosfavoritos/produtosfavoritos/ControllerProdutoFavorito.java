package com.api.produtosfavoritos.produtosfavoritos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/v1/clientes/{id}")
public class ControllerProdutoFavorito {

    Logger log = LoggerFactory.getLogger("ControllerProdutoFavorito");
    private final CadastroProdutoFavorito cadastroProdutoFavorito;

    public ControllerProdutoFavorito(CadastroProdutoFavorito cadastroProdutoFavorito) {
        this.cadastroProdutoFavorito = cadastroProdutoFavorito;
    }

    @RequestMapping(value = "/produtosfavoritos/{id_produto}", method = RequestMethod.POST)
    public ResponseEntity cadastrarProdutoFavorito(@PathVariable(name = "id") String idCliente, @PathVariable(name = "id_produto") String idProduto){

        cadastroProdutoFavorito.cadastrar(idCliente, idProduto);
        log.info("Produto Favorito Cadastrado com sucesso");

        return ResponseEntity.created(URI.create("http://challenge-api.luizalabs.com/api/product/"+ idProduto + "/")).build();
    }

}
