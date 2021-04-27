package com.api.produtosfavoritos.produtofavorito;

import com.api.produtosfavoritos.cliente.ClienteRepository;
import com.api.produtosfavoritos.config.WireMockInitializer;
import com.api.produtosfavoritos.produtosfavoritos.ProdutoFavoritoRepository;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {WireMockInitializer.class})
@DisplayName("Consulta Lista Produtos Favoritos")
public class ConsultaListaProdutosFavoritosTest {

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
        this.wireMockServer.stubFor(get(urlEqualTo("/api/product/958ec015-cfcf-258d-c6df-1721de0ab6ea/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(getBodyProdutos1())));
        this.mockMvc.perform(post(url + "/produtosfavoritos/958ec015-cfcf-258d-c6df-1721de0ab6ea")
                .header("Authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        this.wireMockServer.stubFor(get(urlEqualTo("/api/product/a81e138c-a288-41fa-ab94-8f42d6add281/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(getBodyProdutos2())));
        this.mockMvc.perform(post(url + "/produtosfavoritos/a81e138c-a288-41fa-ab94-8f42d6add281")
                .header("Authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @DisplayName("Devo consultar lista produtos favoritos Caso cliente tenha produtos favoritos")
    @Test
    public void devoConsultarListaProdutosFavoritosEntaoRetornaHttp200() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(url + "/produtosfavoritos")
                .header("Authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseBody()));
    }

    private String getBody(){
        return "{\"nome\":\"helder\",\"email\":\"helder@gmail.com\"}";
    }

    private String getBodyProdutos1(){
        return "{\"id\":\"958ec015-cfcf-258d-c6df-1721de0ab6ea\"," +
                "\"title\":\"Moisés Dorel Windoo 1529\"," +
                "\"image\":\"http://challenge-api.luizalabs.com/images/958ec015-cfcf-258d-c6df-1721de0ab6ea.jpg\"," +
                "\"brand\":\"bébé confort\"," +
                "\"price\": 1149.0 }";
    }

    private String getBodyProdutos2(){
        return "{\"id\":\"a81e138c-a288-41fa-ab94-8f42d6add281\"," +
                "\"title\":\"Micro-ondas Electrolux MEX55 45L Inox\"," +
                "\"image\":\"http://challenge-api.luizalabs.com/images/a81e138c-a288-41fa-ab94-8f42d6add281.jpg\"," +
                "\"brand\":\"electrolux\"," +
                "\"reviewScore\": 2.3333333," +
                "\"price\": 1199.0 }";
    }

    private String getResponseBody(){
        return "{\"content\": [" +
                "{\"id\":\"958ec015-cfcf-258d-c6df-1721de0ab6ea\"," +
                "\"title\":\"Moisés Dorel Windoo 1529\"," +
                "\"image\":\"http://challenge-api.luizalabs.com/images/958ec015-cfcf-258d-c6df-1721de0ab6ea.jpg\"," +
                "\"price\": 1149.0 }, " +
                "{\"id\":\"a81e138c-a288-41fa-ab94-8f42d6add281\"," +
                "\"title\":\"Micro-ondas Electrolux MEX55 45L Inox\"," +
                "\"image\":\"http://challenge-api.luizalabs.com/images/a81e138c-a288-41fa-ab94-8f42d6add281.jpg\"," +
                "\"reviewScore\": 2.3333333," +
                "\"price\": 1199.0 }" +
                "], " +
                "\"pageable\": {" +
                "\"sort\": {" +
                "\"sorted\": false," +
                "\"unsorted\": true," +
                "\"empty\": true" +
                "}," +
                "\"offset\": 0," +
                "\"pageNumber\": 0," +
                "\"pageSize\": 20," +
                "\"paged\": true," +
                "\"unpaged\": false" +
                "}," +
                "\"totalPages\": 1," +
                "\"totalElements\": 2," +
                "\"last\": true," +
                "\"numberOfElements\": 2," +
                "\"first\": true," +
                "\"size\": 20," +
                "\"number\": 0," +
                "\"sort\": {" +
                "\"sorted\": false," +
                "\"unsorted\": true," +
                "\"empty\": true" +
                "}," +
                "\"empty\": false}";
    }
}
