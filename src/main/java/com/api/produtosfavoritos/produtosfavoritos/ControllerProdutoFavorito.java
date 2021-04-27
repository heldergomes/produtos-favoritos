package com.api.produtosfavoritos.produtosfavoritos;

import com.api.produtosfavoritos.cliente.Cliente;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/clientes/{id}")
public class ControllerProdutoFavorito {

    Logger log = LoggerFactory.getLogger("ControllerProdutoFavorito");
    private final CadastroProdutoFavorito cadastroProdutoFavorito;
    private final ProdutoFavoritoRepository produtoFavoritoRepository;

    public ControllerProdutoFavorito(CadastroProdutoFavorito cadastroProdutoFavorito, ProdutoFavoritoRepository produtoFavoritoRepository) {
        this.cadastroProdutoFavorito = cadastroProdutoFavorito;
        this.produtoFavoritoRepository = produtoFavoritoRepository;
    }

    @RequestMapping(value = "/produtosfavoritos/{id_produto}", method = RequestMethod.POST)
    public ResponseEntity cadastrarProdutoFavorito(@PathVariable(name = "id") String idCliente, @PathVariable(name = "id_produto") String idProduto){

        cadastroProdutoFavorito.cadastrar(idCliente, idProduto);
        log.info("Produto Favorito Cadastrado com sucesso");

        return ResponseEntity.created(URI.create("http://challenge-api.luizalabs.com/api/product/"+ idProduto + "/")).build();
    }

    @GetMapping(value = "/produtosfavoritos")
    public ResponseEntity<Page<ProdutoDto>> consultarProdutosFavoritos(Pageable pageable) {
        Page<ProdutoFavorito> produtoFavorito = produtoFavoritoRepository.findAll(pageable);
        log.info("Consulta da lista de produtos favoritos - pageNumber: " + pageable.getPageNumber() + " pageSize: " + pageable.getPageSize());
        Page<ProdutoDto> dto = produtoFavorito.map( produto -> new ModelMapper().map(produto, ProdutoDto.class));
        return ResponseEntity.ok(dto);
    }


}
