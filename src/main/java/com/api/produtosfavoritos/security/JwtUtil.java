package com.api.produtosfavoritos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String geradorToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                .compact();
    }

    public boolean validadorToken(String token) {
        Claims claims = getClaims(token);
        if (claims != null) {
            String login = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            Date now = new Date(System.currentTimeMillis());
            if (login != null && expirationDate != null && now.before(expirationDate))
                return true;
        }
        return false;
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
        }catch (Exception ex){
            return null;
        }
    }

    public String getLogin(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }
}
