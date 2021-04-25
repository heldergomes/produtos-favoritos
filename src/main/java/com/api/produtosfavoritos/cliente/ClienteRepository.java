package com.api.produtosfavoritos.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {

    @Query(value = "SELECT * FROM CLIENTE WHERE EMAIL = ?1", nativeQuery = true)
    Optional<Cliente> getByEmail(String email);
}
