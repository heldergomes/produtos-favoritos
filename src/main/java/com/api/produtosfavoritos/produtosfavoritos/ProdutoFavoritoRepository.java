package com.api.produtosfavoritos.produtosfavoritos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoFavoritoRepository extends JpaRepository<ProdutoFavorito, String> {

    @Query(value = "SELECT * FROM PRODUTOS_FAVORITOS WHERE CLIENTE_ID = ?1 AND PRODUTO_ID = ?2", nativeQuery = true)
    Optional<ProdutoFavorito> get(String idCliente, String idProduto);

}
