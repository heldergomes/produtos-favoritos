package com.api.produtosfavoritos.produto;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

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

    @RequestMapping(value = "/produtos/{id_produto}", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity cadastrarProdutoFavorito(@PathVariable(name = "id") String idCliente,
                                                   @PathVariable(name = "id_produto") String idProduto,
                                                   @Valid @RequestBody StatusDto dto){

        cadastroProdutoFavorito.cadastrar(idCliente, idProduto, dto.getStatus());
        log.info("Produto Favorito Cadastrado com sucesso");

        return ResponseEntity.created(URI.create("http://challenge-api.luizalabs.com/api/product/"+ idProduto + "/")).build();
    }

    @GetMapping(value = "/produtos", produces = "application/json; charset=utf-8")
    public ResponseEntity<Page<ProdutoDto>> consultarProdutosFavoritos(@PathVariable(name = "id") String idCliente,
                                                                       @RequestParam String status, Pageable pageable) {
        Page<Produto> produtoFavorito = produtoFavoritoRepository.findByIdClienteAndStatus(idCliente, status, pageable);
        log.info("Consulta da lista de produtos favoritos - pageNumber: " + pageable.getPageNumber() + " pageSize: " + pageable.getPageSize());
        Page<ProdutoDto> dto = produtoFavorito.map( produto -> new ModelMapper().map(produto, ProdutoDto.class));
        return ResponseEntity.ok(dto);
    }


}
