package com.api.produtosfavoritos.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

    Logger log = LoggerFactory.getLogger("JwtAuthenticationFailureHandler");
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setStatus(401);
        httpServletResponse.getWriter().append(json());
        httpServletResponse.setHeader("Content-Type", "application/json");
        log.warn("Acesso a aplicacao não autorizado");
    }

    private String json(){
        long date = new Date().getTime();
        return "{\"timestamp\": "+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date) + ", "
                + "\"status\": 401, "
                + "\"error\": \"Não autorizado\", "
                + "\"message\": \"Email ou Nome inválido\", "
                + "\"path\": \"/login\"}";
    }
}
