package com.api.produtosfavoritos.cliente;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DeletarClienteTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository repository;

    @DisplayName("Devo deletar o cliente Caso ele exista")
    @Test
    public void devoDeletarOClienteEntaoRetornaHttp200() throws Exception {
        this.repository.deleteAll();
        ResultActions response = this.mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBody()))
                .andExpect(status().isCreated());
        String url = response.andReturn().getResponse().getHeader("Location");

        this.mockMvc.perform(delete(url))
                .andExpect(status().isOk());
    }

    @DisplayName("Devo retornar cliente Nao Encontrado Caso cliente nao exista")
    @Test
    public void devoRetornarClienteNaoEncontradoEntaoRetornaHttp404() throws Exception {
        this.repository.deleteAll();
        this.mockMvc.perform(delete("/api/v1/clientes/a267c21f-78fb-4745-a299-412f8a7f363d6"))
                .andExpect(status().isNotFound());
    }

    private String getBody(){
        return "{\"nome\":\"helder\",\"email\":\"helder@gmail.com\"}";
    }
}
