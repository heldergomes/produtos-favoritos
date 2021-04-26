package com.api.produtosfavoritos.produtosfavoritos;

import com.api.produtosfavoritos.cliente.Cliente;
import com.api.produtosfavoritos.cliente.ClienteRepository;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class CadastroProdutoFavorito {

    Logger log = LoggerFactory.getLogger("CadastroProdutoFavorito");

    private final ClienteRepository clienteRepository;
    private final ProdutoApiRequest produtoApiRequest;
    private final ProdutoFavoritoRepository produtoFavoritoRepository;
    public CadastroProdutoFavorito(ClienteRepository clienteRepository, ProdutoApiRequest produtoApiRequest, ProdutoFavoritoRepository produtoFavoritoRepository) {
        this.clienteRepository = clienteRepository;
        this.produtoApiRequest = produtoApiRequest;
        this.produtoFavoritoRepository = produtoFavoritoRepository;
    }

    public void cadastrar(String idCliente, String idProduto){
        validarExistenciaCliente(idCliente);
        validarNaoExistenciaProdutoFavorito(idCliente, idProduto);
        ProdutoFavorito produtoFavorito = consultarCatalogoProduto(idProduto);
        salvarProdutoFavorito(produtoFavorito, idCliente);
    }

    private void validarExistenciaCliente(String idCliente){
        Optional<Cliente> cliente = Optional.of(clienteRepository.getOne(idCliente));
        cliente.orElseThrow(EntityNotFoundException::new);
        log.info("Cliente validado com sucesso: ", cliente.get().toString());
    }

    private void validarNaoExistenciaProdutoFavorito(String idCliente, String idProduto){
        Optional<ProdutoFavorito> produtoFavorito = produtoFavoritoRepository.get(idCliente, idProduto);
        if (produtoFavorito.isPresent())
            throw new DataIntegrityViolationException("Produto ja cadastrado como favorito");
        log.info("produto validado com sucesso");

    }

    private ProdutoFavorito consultarCatalogoProduto(String idProduto){
        try {
            ResponseEntity<ProdutoDto> dto = produtoApiRequest.getProduto(idProduto);
            log.info("produto consultado no catalogo de produtos com sucesso");
            return new ModelMapper().map(dto.getBody(), ProdutoFavorito.class);
        }catch (FeignException ex){
            throw new EntityNotFoundException("Produto nao encontrado no catalogo - " + ex.status());
        }
    }

    private void salvarProdutoFavorito(ProdutoFavorito produtoFavorito, String idCliente){
        produtoFavorito.setIdCliente(idCliente);
        produtoFavoritoRepository.save(produtoFavorito);
        log.info("produto salvo na lista de produtos favoritos com sucesso");
    }
}
