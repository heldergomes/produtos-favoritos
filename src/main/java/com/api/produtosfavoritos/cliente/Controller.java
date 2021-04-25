package com.api.produtosfavoritos.cliente;

import org.hibernate.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1")
public class Controller {

    Logger log = LoggerFactory.getLogger("Controller");
    private final ClienteRepository clienteRepository;

    public Controller(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @RequestMapping(value = "/clientes", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity cadastrarCliente(@Valid @RequestBody ClienteDto dto){

        Cliente cliente = new ModelMapper().map(dto, Cliente.class);
        log.info("Mapeamento do cliente realizado com sucesso: " + cliente.toString());

        cliente.setUUID();
        cliente = clienteRepository.save(cliente);
        log.info("Cliente salvo com sucesso: " + cliente.toString());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cliente.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value = "/clientes/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<ClienteDto> buscarCliente(@PathVariable String id){

        Optional<Cliente> cliente = Optional.of(clienteRepository.getOne(id));
        log.info("Cliente consultado com sucesso: ");

        cliente.orElseThrow(EntityNotFoundException::new);

        ClienteDto dto = new ModelMapper().map(cliente.get(), ClienteDto.class);
        log.info("Mapeamento do dto feito com sucesso: " + cliente.get().toString());

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/clientes/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<ClienteDto> atualizarCliente(@PathVariable String id, @Valid @RequestBody ClienteDto dto){

        Optional<Cliente> clienteOptional = Optional.of(clienteRepository.getOne(id));
        log.info("Cliente consultado com sucesso: ");
        clienteOptional.orElseThrow(EntityNotFoundException::new);

        Cliente cliente = new ModelMapper().map(dto, Cliente.class);
        cliente.setId(id);
        log.info("Mapeamento do cliente realizado com sucesso: " + cliente.toString());

        clienteRepository.save(cliente);
        log.info("Cliente atualizado com sucesso: " + cliente.toString());

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/clientes/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ClienteDto> deletarCliente(@PathVariable String id){

        clienteRepository.deleteById(id);
        log.info("Cliente deletado com sucesso: " + id);

        return ResponseEntity.ok().build();
    }
}
