package com.api.produtosfavoritos.cliente;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "cliente")
@Table(name = "cliente")
public class Cliente {

    @Id
    @Column(name = "cliente_id")
    private String id;
    private String nome;
    private String email;

    @Override
    public String toString() {
        return "Cliente{" +
                "nome='" + nome + '\'' +
                '}';
    }

    public void setUUID(){
        this.id = String.valueOf(UUID.randomUUID());
    }
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
