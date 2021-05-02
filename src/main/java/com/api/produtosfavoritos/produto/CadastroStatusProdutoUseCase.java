package com.api.produtosfavoritos.produto;

import com.api.produtosfavoritos.cliente.Cliente;
import com.api.produtosfavoritos.cliente.ClienteRepository;
import com.api.produtosfavoritos.exception.EntidadeNaoEncontradaException;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class CadastroStatusProdutoUseCase {

    Logger log = LoggerFactory.getLogger("CadastroProdutoFavorito");

    private final ClienteRepository clienteRepository;
    private final ProdutoApiRequest produtoApiRequest;
    private final ProdutoRepository produtoRepository;
    public CadastroStatusProdutoUseCase(ClienteRepository clienteRepository, ProdutoApiRequest produtoApiRequest, ProdutoRepository produtoRepository) {
        this.clienteRepository = clienteRepository;
        this.produtoApiRequest = produtoApiRequest;
        this.produtoRepository = produtoRepository;
    }

    public void cadastrar(String idCliente, String idProduto, String status){
        StatusProduto.validaStatus(status);
        validarExistenciaCliente(idCliente);
        validarNaoExistenciaProdutoFavorito(idCliente, idProduto);
        Produto produto = consultarCatalogoProduto(idProduto);
        salvarProdutoFavorito(produto, idCliente, status);
    }

    private void validarExistenciaCliente(String idCliente){
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        cliente.orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente: " + idCliente + " nao encontrado"));
        log.info("Cliente validado com sucesso: " + cliente.get().toString());
    }

    private void validarNaoExistenciaProdutoFavorito(String idCliente, String idProduto){
        Optional<Produto> produtoFavorito = produtoRepository.get(idProduto, idCliente);
        if (produtoFavorito.isPresent())
            throw new DataIntegrityViolationException("Produto ja cadastrado como favorito");
        log.info("produto validado com sucesso: " + produtoFavorito);
    }

    private Produto consultarCatalogoProduto(String idProduto){
        try {
            ResponseEntity<ProdutoDto> dto = produtoApiRequest.getProduto(idProduto);
            log.info("produto consultado no catalogo de produtos com sucesso: " + dto.toString());
            return new ModelMapper().map(dto.getBody(), Produto.class);
        }catch (FeignException ex){
            throw new EntidadeNaoEncontradaException("Produto nao encontrado no catalogo - " + ex.status());
        }
    }

    private void salvarProdutoFavorito(Produto produto, String idCliente, String status){
        produto.setUUID();
        produto.setIdCliente(idCliente);
        produto.setStatusProduto(Arrays.asList(status));
        produtoRepository.save(produto);
        log.info("produto salvo na lista de produtos favoritos com sucesso");
    }
}
