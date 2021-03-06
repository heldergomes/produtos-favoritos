package com.api.produtosfavoritos.cliente;

import com.api.produtosfavoritos.ProdutosFavoritosApplication;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ProdutosFavoritosApplication.class)
@AutoConfigureMockMvc
@DisplayName("Consulta Cliente")
public class ConsultaClienteTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository repository;
    String url = "";
    String authorization = "";

    @BeforeEach
    public void setup() throws Exception {
        this.repository.deleteAll();
        ResultActions response = this.mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBody()))
                .andExpect(status().isCreated());
        url = response.andReturn().getResponse().getHeader("Location");
        authorization = this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBody())).andReturn().getResponse().getHeader("Authorization");
    }

    @DisplayName("Devo consultar o cliente Caso ele exista")
    @Test
    public void devoConsultarOClienteEntaoRetornaHttp200eRecurso() throws Exception {
        this.mockMvc.perform(get(url)
                .header("Authorization", authorization))
                .andExpect(status().isOk())
                .andExpect(content().json(getBodyWithId(url.substring(33))));
    }

    @DisplayName("Devo retornar cliente Nao Encontrado Caso cliente nao exista")
    @Test
    public void devoRetornarClienteNaoEncontradoEntaoRetornaHttp404() throws Exception {
        this.mockMvc.perform(get("/api/v1/clientes/a267c21f-78fb-4745-a299-412f8a7f363d6")
                .header("Authorization", authorization))
                .andExpect(status().isNotFound());
    }

    private String getBody(){
        return "{\"nome\":\"helder\",\"email\":\"helder@gmail.com\"}";
    }

    private String getBodyWithId(String id){
        return "{\"id\":\""+ id +"\",\"nome\":\"helder\",\"email\":\"helder@gmail.com\"}";
    }
}
