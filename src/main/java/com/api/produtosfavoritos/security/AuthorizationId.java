package com.api.produtosfavoritos.security;

import com.api.produtosfavoritos.exception.AutorizacaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationId {

    Logger logger = LoggerFactory.getLogger(AuthorizationId.class);

    public void valid(String id){
        ClienteSecurity cliente = (ClienteSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (cliente == null  || !id.equals(cliente.getId())) {
            logger.error("usuario negado devido falta de acesso !");
            throw new AutorizacaoException("Acesso Negado !");
        }
    }
}
