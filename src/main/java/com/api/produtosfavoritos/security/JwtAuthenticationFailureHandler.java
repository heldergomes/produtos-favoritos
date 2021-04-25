package com.api.produtosfavoritos.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setStatus(401);
        httpServletResponse.getWriter().append(json());
        httpServletResponse.setHeader("Content-Type", "application/json");
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
