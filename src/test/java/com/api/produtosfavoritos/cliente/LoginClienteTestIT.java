package com.api.produtosfavoritos.cliente;

import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginClienteTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository repository;

    @BeforeEach
    public void setup() throws Exception {
        this.repository.deleteAll();
        ResultActions response = this.mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBody()))
                .andExpect(status().isCreated());
    }

    @DisplayName("Devo fazer login do cliente Caso ele exista")
    @Test
    public void devoFazerOLoginDoClienteEntaoRetornaHttp200() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBody()))
                .andExpect(status().isOk());
    }

    @DisplayName("Nao Devo fazer login do cliente Caso o Email Nao Exista")
    @Test
    public void naoDevoFazerOLoginDoClienteSeEmailNaoExistaEntaoRetornaHttp401() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBody().replace("\"helder@gmail.com\"", "\"helder22@gmail.com\"")))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Nao Devo fazer login do cliente Caso o Nome Nao Exista")
    @Test
    public void naoDevoFazerOLoginDoClienteSeNomeNaoExisteEntaoRetornaHttp401() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBody().replace("\"helder\"", "\"helder22\"")))
                .andExpect(status().isUnauthorized());
    }

    private String getBody(){
        return "{\"nome\":\"helder\",\"email\":\"helder@gmail.com\"}";
    }

}
