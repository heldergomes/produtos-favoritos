package com.api.produtosfavoritos.cliente;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CadastrarClienteTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository repository;

    @DisplayName("Devo cadastrar o cliente Caso o email nao esteja cadastrado e o nome esteja preenchido")
    @Test
    public void devoCadastrarOClienteEntaoRetornaHttp201() throws Exception {
        this.repository.deleteAll();
        this.mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBody()))
                .andExpect(status().isCreated());
    }

    @DisplayName("Nao Devo cadastrar o cliente Caso o email ja esteja cadastrado")
    @Test
    public void naoDevoCadastrarOClienteComEmailJaCadastradoEntaoRetornaHttp409() throws Exception {
        this.repository.deleteAll();
        this.mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBody()))
                .andExpect(status().isCreated());
        this.mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBody()))
                .andExpect(status().isConflict());
    }

    @DisplayName("Nao Devo cadastrar o cliente Caso o email esteja vazio")
    @Test
    public void naoDevoCadastrarOClienteComEmailVazioEntaoRetornaHttp400() throws Exception {
        this.repository.deleteAll();
        this.mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBody().replace("\"helder@gmail.com\"", "\"\"")))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Nao Devo cadastrar o cliente Caso o nome esteja vazio")
    @Test
    public void naoDevoCadastrarOClienteComNomeVazioEntaoRetornaHttp400() throws Exception {
        this.repository.deleteAll();
        this.mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Correlation-ID",String.valueOf(UUID.randomUUID()))
                .content(getBody().replace("\"helder\"", "\"\"")))
                .andExpect(status().isBadRequest());
    }

    private String getBody(){
        return "{\"nome\":\"helder\",\"email\":\"helder@gmail.com\"}";
    }
}
