package com.api.produtosfavoritos.cliente;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

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

        ModelMapper modelMapper = new ModelMapper();
        Cliente cliente = modelMapper.map(dto, Cliente.class);
        log.info("Mapeamento do cliente realizado com sucesso: " + cliente.toString());

        cliente.setUUID();
        cliente = clienteRepository.save(cliente);
        log.info("Cliente salvo com sucesso: " + cliente.toString());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cliente.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
