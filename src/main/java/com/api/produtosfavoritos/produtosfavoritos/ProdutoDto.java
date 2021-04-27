package com.api.produtosfavoritos.produtosfavoritos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProdutoDto {
    @JsonProperty(value = "id")
    private String id;
    @JsonProperty(value = "image")
    private String image;
    @JsonProperty(value = "price")
    private BigDecimal price;
    @JsonProperty(value = "title")
    private String title;
    @JsonProperty(value = "reviewScore")
    private BigDecimal reviewScore;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
