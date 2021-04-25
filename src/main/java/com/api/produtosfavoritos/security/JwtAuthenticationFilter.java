package com.api.produtosfavoritos.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private static final String secretPassword = "263e0086-14f7-43d3-9aa9-a5848799571a";

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil){
        setAuthenticationFailureHandler(new JwtAuthenticationFailureHandler());
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            CredenciaisDTO dto = new ObjectMapper().readValue(request.getInputStream(), CredenciaisDTO.class);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), secretPassword, new ArrayList<>());
            Authentication authentication = authenticationManager.authenticate(authToken);
            validateNameWithEmail(dto, authentication);
            return authentication;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String email = ((ClienteSecurity) authResult.getPrincipal()).getUsername();
        String token = jwtUtil.geradorToken(email);
        String id = ((ClienteSecurity) authResult.getPrincipal()).getId();
        response.setHeader("Authorization", "Bearer " + token);
        response.setContentType("application/json");
        response.getWriter()
                .append("{\"" + "id\": \"" + id +"\" }");
    }

    private void validateNameWithEmail(CredenciaisDTO dto, Authentication auth){
        if (!dto.getNome().equals(((ClienteSecurity) auth.getPrincipal()).getNome())) {
            throw new UsernameNotFoundException("Nome inválido");
        }
    }
}
