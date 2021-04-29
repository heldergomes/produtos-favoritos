package com.api.produtosfavoritos.produto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoFavoritoRepository extends MongoRepository<Produto, String> {

    @Query("{ 'id' : '?0',  'idCliente' : '?1'}")
    Optional<Produto> get(String id, String idCliente);

    Page<Produto> findByIdClienteAndStatus(String idCliente, String status, Pageable pageable);
}
