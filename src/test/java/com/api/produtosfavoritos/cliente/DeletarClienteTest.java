package com.api.produtosfavoritos.cliente;

import com.api.produtosfavoritos.config.WireMockInitializer;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {WireMockInitializer.class})
@DisplayName("Delecao Cliente")
public class DeletarClienteTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WireMockServer wireMockServer;
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

    @DisplayName("Devo deletar o cliente Caso ele exista")
    @Test
    public void devoDeletarOClienteEntaoRetornaHttp200() throws Exception {
        this.mockMvc.perform(delete(url)
                .header("Authorization", authorization))
                .andExpect(status().isOk());
    }

    @DisplayName("Devo deletar o cliente e os produtos do cliente Caso ele exista e tenha produtos vinculados")
    @Test
    public void devoDeletarOClienteEProdutosEntaoRetornaHttp200() throws Exception {
        this.wireMockServer.stubFor(get(urlEqualTo("/api/product/958ec015-cfcf-258d-c6df-1721de0ab6ea/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(getBodyProdutos())));
        this.mockMvc.perform(post(url + "/produtos/958ec015-cfcf-258d-c6df-1721de0ab6ea")
                .header("Authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBodyStatus()))
                .andExpect(status().isCreated());
        this.mockMvc.perform(delete(url)
                .header("Authorization", authorization))
                .andExpect(status().isOk());
    }

    @DisplayName("Devo retornar cliente Nao Encontrado Caso cliente nao exista")
    @Test
    public void devoRetornarClienteNaoEncontradoEntaoRetornaHttp404() throws Exception {
        this.mockMvc.perform(delete("/api/v1/clientes/a267c21f-78fb-4745-a299-412f8a7f363d6")
                .header("Authorization", authorization))
                .andExpect(status().isNotFound());
    }

    private String getBody(){
        return "{\"nome\":\"helder\",\"email\":\"helder@gmail.com\"}";
    }

    private String getBodyProdutos(){
        return "{\"id\":\"958ec015-cfcf-258d-c6df-1721de0ab6ea\"," +
                "\"title\":\"Moisés Dorel Windoo 1529\"," +
                "\"image\":\"http://challenge-api.luizalabs.com/images/958ec015-cfcf-258d-c6df-1721de0ab6ea.jpg\"," +
                "\"brand\":\"bébé confort\"," +
                "\"price\": 1149.0 }";
    }

    private String getBodyStatus(){
        return "{\"status\":\"favorito\"}";
    }
}
