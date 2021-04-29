package com.api.produtosfavoritos.produto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class StatusDto {

    @NotBlank(message = "status não pode estar nulo")
    @JsonProperty(value = "status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
