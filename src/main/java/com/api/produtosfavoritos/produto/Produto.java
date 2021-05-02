package com.api.produtosfavoritos.produto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Document(collection = "produtos")
@CompoundIndexes({
        @CompoundIndex(name = "id_produto_cliente", def = "{'idProduto' : 1, 'idCliente': 1}")
})
public class Produto {

    @Id
    private String id;
    @Indexed
    private String idProduto;
    @Indexed
    private String idCliente;
    private String image;
    private BigDecimal price;
    private String title;
    private BigDecimal reviewScore;
    private List<String> status;

    public List<String> getStatusProduto() {
        return status;
    }

    public void setUUID(){
        this.id = String.valueOf(UUID.randomUUID());
    }

    public void setStatusProduto(List<String> status) {
        this.status = status;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }


    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getReviewScore() {
        return reviewScore;
    }

    public void setReviewScore(BigDecimal reviewScore) {
        this.reviewScore = reviewScore;
    }
}
