package com.api.produtosfavoritos.cliente;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente, String> {

    @Query("{ 'email' : '?0'}")
    Optional<Cliente> getByEmail(String email);
}
