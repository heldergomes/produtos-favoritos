package com.api.produtosfavoritos.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtUtil jwtUtil;
    private ConsultaClienteSecurity consultaClienteSecurity;
    private static final String secretPassword = "263e0086-14f7-43d3-9aa9-a5848799571a";

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  JwtUtil jwtUtil,
                                  ConsultaClienteSecurity consultaClienteSecurity) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.consultaClienteSecurity = consultaClienteSecurity;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            UsernamePasswordAuthenticationToken auth = getAuthentication(authorization.substring(7));
            if (auth != null){
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        if (jwtUtil.validadorToken(token)){
            String email = jwtUtil.getLogin(token);
            UserDetails clienteSecurity = consultaClienteSecurity.loadUserByUsername(email);
            return new UsernamePasswordAuthenticationToken(clienteSecurity, secretPassword);
        }
        return null;
    }
}

