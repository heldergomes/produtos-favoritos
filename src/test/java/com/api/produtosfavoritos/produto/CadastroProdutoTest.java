package com.api.produtosfavoritos.produto;

import com.api.produtosfavoritos.cliente.ClienteRepository;
import com.api.produtosfavoritos.config.WireMockInitializer;
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

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {WireMockInitializer.class})
@DisplayName("Cadastro Produto Favorito")
public class CadastroProdutoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    String url = "";
    String authorization = "";

    @BeforeEach
    public void setup() throws Exception {
        this.produtoRepository.deleteAll();
        this.clienteRepository.deleteAll();
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
        this.mockMvc.perform(post(url + "/produtos/958ec015-cfcf-258d-c6df-1721de0ab6ea")
                .header("Authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBodyStatus()))
                .andExpect(status().isCreated());
    }

    @DisplayName("Nao devo criar produto favorito Caso status esta vazio")
    @Test
    public void naoDevoCriarProdutoFavoritoCasoStatusNuloEntaoRetornaHttp400() throws Exception {
        this.mockMvc.perform(post(url + "/produtos/958ec015-cfcf-258d-c6df-1721de0ab6ea")
                .header("Authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Nao devo criar produto favorito Caso produto ja for favorito")
    @Test
    public void naoDevoCriarProdutoFavoritoEntaoRetornaHttp409() throws Exception {
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
        this.mockMvc.perform(post(url + "/produtos/958ec015-cfcf-258d-c6df-1721de0ab6ea")
                .header("Authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBodyStatus()))
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
        this.mockMvc.perform(post(url + "/produtos/958ec015-cfcf-258d-c6df-1721de0ab6ea")
                .header("Authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBodyStatus()))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Nao devo criar produto favorito Caso status seja diferente de favorito")
    @Test
    public void naoDevoCriarProdutoFavoritoCasoStatusNaoSejaFavoritoEntaoRetornaHttp404() throws Exception {
        this.wireMockServer.stubFor(get(urlEqualTo("/api/product/958ec015-cfcf-258d-c6df-1721de0ab6eaXX/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(getBodyProdutos())));
        this.mockMvc.perform(post(url + "/produtos/958ec015-cfcf-258d-c6df-1721de0ab6ea")
                .header("Authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBodyDiferenteStatus()))
                .andExpect(status().isNotFound());
    }

    @AfterEach
    public void afterEach() {
        this.wireMockServer.resetToDefaultMappings();
    }

    private String getBody(){
        return "{\"nome\":\"helder\",\"email\":\"helder@gmail.com\"}";
    }

    private String getBodyDiferenteStatus(){
        return "{\"status\":\"comprado\"}";
    }

    private String getBodyStatus(){
        return "{\"status\":\"favorito\"}";
    }


    private String getBodyProdutos(){
        return "{\"id\":\"958ec015-cfcf-258d-c6df-1721de0ab6ea\"," +
                "\"title\":\"Moisés Dorel Windoo 1529\"," +
                "\"image\":\"http://challenge-api.luizalabs.com/images/958ec015-cfcf-258d-c6df-1721de0ab6ea.jpg\"," +
                "\"brand\":\"bébé confort\"," +
                "\"price\": 1149.0 }";
    }
}
