package com.api.produtosfavoritos.cliente;

import com.api.produtosfavoritos.exception.ChaveDuplicadaException;
import com.api.produtosfavoritos.exception.EntidadeNaoEncontradaException;
import com.api.produtosfavoritos.produto.Produto;
import com.api.produtosfavoritos.produto.ProdutoRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1")
public class ClienteController {

    Logger log = LoggerFactory.getLogger("Controller");

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ProdutoRepository produtoRepository;

    @RequestMapping(value = "/clientes", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity cadastrarCliente(@Valid @RequestBody ClienteDto dto){

        Cliente cliente = new ModelMapper().map(dto, Cliente.class);
        log.info("Mapeamento do cliente realizado com sucesso: " + cliente.toString());

        Optional<Cliente> clienteValidado = clienteRepository.getByEmail(cliente.getEmail());
        if (!clienteValidado.isEmpty())
            throw new ChaveDuplicadaException("Email Ja Cadastrado !");

        cliente.setUUID();
        cliente = clienteRepository.save(cliente);
        log.info("Cliente salvo com sucesso: " + cliente.toString());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cliente.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value = "/clientes/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<ClienteDto> buscarCliente(@PathVariable String id){

        Optional<Cliente> cliente = clienteRepository.findById(id);
        log.info("Cliente consultado com sucesso: " + cliente);

        cliente.orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente: " + id + " nao encontrado"));

        ClienteDto dto = new ModelMapper().map(cliente.get(), ClienteDto.class);
        log.info("Mapeamento do dto feito com sucesso: " + cliente.get().toString());

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/clientes/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<ClienteDto> atualizarCliente(@PathVariable String id, @Valid @RequestBody ClienteDto dto){

        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        log.info("Cliente consultado com sucesso: " + clienteOptional);
        clienteOptional.orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente: " + id + " nao encontrado"));

        Cliente cliente = new ModelMapper().map(dto, Cliente.class);
        cliente.setId(id);
        log.info("Mapeamento do cliente realizado com sucesso: " + cliente.toString());

        clienteRepository.save(cliente);
        log.info("Cliente atualizado com sucesso: " + cliente.toString());

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/clientes/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ClienteDto> deletarCliente(@PathVariable String id){

        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        log.info("Cliente consultado com sucesso: "  + clienteOptional);
        clienteOptional.orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente: " + id + " nao encontrado"));
        Optional<List<Produto>> listaProdutos = produtoRepository.findByIdCliente(id);
        if (!listaProdutos.get().isEmpty()) {
            produtoRepository.deleteAll(listaProdutos.get());
            log.info("Produtos do cliente deletados com sucesso!");
        }
        clienteRepository.delete(clienteOptional.get());
        log.info("Cliente deletado com sucesso: " + id);

        return ResponseEntity.ok().build();
    }
}
