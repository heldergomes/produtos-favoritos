package com.api.produtosfavoritos.produtofavorito;

import com.api.produtosfavoritos.cliente.ClienteRepository;
import com.api.produtosfavoritos.config.WireMockInitializer;
import com.api.produtosfavoritos.produtosfavoritos.ProdutoFavoritoRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {WireMockInitializer.class})
@DisplayName("Cadastro Produto Favorito")
public class CadastroProdutoFavoritoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private ClienteRepository repositoryCliente;
    @Autowired
    private ProdutoFavoritoRepository produtoFavoritoRepository;
    String url = "";
    String authorization = "";

    @BeforeEach
    public void setup() throws Exception {
        this.produtoFavoritoRepository.deleteAll();
        this.repositoryCliente.deleteAll();
        ResultActions response = this.mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBody()))
                .andExpect(status().isCreated());
        url = response.andReturn().getResponse().getHeader("Location");
        authorization = this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBody())).andReturn().getResponse().getHeader("Authorization");
    }

    @DisplayName("Devo criar produto favorito Caso cliente exista E produto nao esteja favorito E produto exista")
    @Test
    public void devoCriarProdutoFavoritoEntaoRetornaHttp201() throws Exception {
        this.wireMockServer.stubFor(get(urlEqualTo("/api/product/958ec015-cfcf-258d-c6df-1721de0ab6ea/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(getBodyProdutos())));
        this.mockMvc.perform(post(url + "/produtosfavoritos/958ec015-cfcf-258d-c6df-1721de0ab6ea")
                .header("Authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @DisplayName("Nao devo criar produto favorito Caso cliente Nao exista")
    @Test
    public void naoDevoCriarProdutoFavoritoEntaoRetornaHttp404() throws Exception {
        this.wireMockServer.stubFor(get(urlEqualTo("/api/product/958ec015-cfcf-258d-c6df-1721de0ab6ea/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(getBodyProdutos())));
        this.mockMvc.perform(post(url + "11/produtosfavoritos/958ec015-cfcf-258d-c6df-1721de0ab6ea")
                .header("Authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Nao devo criar produto favorito Caso produto ja for favorito")
    @Test
    public void naoDevoCriarProdutoFavoritoEntaoRetornaHttp409() throws Exception {
        this.wireMockServer.stubFor(get(urlEqualTo("/api/product/958ec015-cfcf-258d-c6df-1721de0ab6ea/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(getBodyProdutos())));
        this.mockMvc.perform(post(url + "/produtosfavoritos/958ec015-cfcf-258d-c6df-1721de0ab6ea")
                .header("Authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        this.mockMvc.perform(post(url + "/produtosfavoritos/958ec015-cfcf-258d-c6df-1721de0ab6ea")
                .header("Authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @DisplayName("Nao devo criar produto favorito Caso produto nao exista")
    @Test
    public void naoDevoCriarProdutoFavoritoCasoProdutoNaoExistaEntaoRetornaHttp404() throws Exception {
        this.wireMockServer.stubFor(get(urlEqualTo("/api/product/958ec015-cfcf-258d-c6df-1721de0ab6eaXX/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(getBodyProdutos())));
        this.mockMvc.perform(post(url + "/produtosfavoritos/958ec015-cfcf-258d-c6df-1721de0ab6ea")
                .header("Authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @AfterEach
    public void afterEach() {
        this.wireMockServer.resetToDefaultMappings();
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
}