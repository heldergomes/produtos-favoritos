package com.api.produtosfavoritos.cliente;

import com.api.produtosfavoritos.exception.ChaveDuplicadaException;
import com.api.produtosfavoritos.exception.EntidadeNaoEncontradaException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1")
public class ControllerCliente {

    Logger log = LoggerFactory.getLogger("Controller");

    @Autowired
    private ClienteRepository repository;

    @RequestMapping(value = "/clientes", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity cadastrarCliente(@Valid @RequestBody ClienteDto dto){

        Cliente cliente = new ModelMapper().map(dto, Cliente.class);
        log.info("Mapeamento do cliente realizado com sucesso: " + cliente.toString());

        Optional<Cliente> clienteValidado = repository.getByEmail(cliente.getEmail());
        if (!clienteValidado.isEmpty())
            throw new ChaveDuplicadaException("Email Ja Cadastrado !");

        cliente.setUUID();
        cliente = repository.save(cliente);
        log.info("Cliente salvo com sucesso: " + cliente.toString());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cliente.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value = "/clientes/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<ClienteDto> buscarCliente(@PathVariable String id){

        Optional<Cliente> cliente = repository.findById(id);
        log.info("Cliente consultado com sucesso: ");

        cliente.orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente: " + id + " nao encontrado"));

        ClienteDto dto = new ModelMapper().map(cliente.get(), ClienteDto.class);
        log.info("Mapeamento do dto feito com sucesso: " + cliente.get().toString());

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/clientes/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<ClienteDto> atualizarCliente(@PathVariable String id, @Valid @RequestBody ClienteDto dto){

        Optional<Cliente> clienteOptional = repository.findById(id);
        log.info("Cliente consultado com sucesso: ");
        clienteOptional.orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente: " + id + " nao encontrado"));

        Cliente cliente = new ModelMapper().map(dto, Cliente.class);
        cliente.setId(id);
        log.info("Mapeamento do cliente realizado com sucesso: " + cliente.toString());

        repository.save(cliente);
        log.info("Cliente atualizado com sucesso: " + cliente.toString());

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/clientes/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ClienteDto> deletarCliente(@PathVariable String id){

        Optional<Cliente> clienteOptional = repository.findById(id);
        log.info("Cliente consultado com sucesso: ");
        clienteOptional.orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente: " + id + " nao encontrado"));

        repository.delete(clienteOptional.get());
        log.info("Cliente deletado com sucesso: " + id);

        return ResponseEntity.ok().build();
    }
}
