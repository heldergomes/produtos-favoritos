package com.api.produtosfavoritos.security;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CredenciaisDTO {

    @NotNull(message = "O campo nome não pode ser nulo")
    @NotEmpty(message = "O campo nome não pode estar vazio")
    private String nome;
    @NotNull(message = "O campo email não pode ser nulo")
    @NotEmpty(message = "O campo email não pode estar vazio")
    private String email;

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}
