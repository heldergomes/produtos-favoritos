package com.api.produtosfavoritos.cliente;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class ClienteDto {

    @JsonProperty("id")
    private String id;
    @JsonProperty("nome")
    @NotBlank(message = "nome não pode estar nulo")
    private String nome;
    @JsonProperty("email")
    @NotBlank(message = "email não pode estar nulo")
    private String email;

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
