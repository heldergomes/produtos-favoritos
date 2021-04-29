package com.api.produtosfavoritos.produto;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
        name = "${produtos_client.name}",
        url = "${produtos_client.url}"
)
public interface ProdutoApiRequest {
    @RequestMapping(method = RequestMethod.GET, value = "/api/product/{id}/")
    ResponseEntity<ProdutoDto> getProduto(@PathVariable("id") String idProduto);
}
