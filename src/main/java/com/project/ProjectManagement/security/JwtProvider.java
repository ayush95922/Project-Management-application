package com.project.ProjectManagement.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


// used by the createUser method of controller/service, creates a user, generates the token and also set the authentication
public class JwtProvider {
    static SecretKey key = JwtTokenValidator.secretKey;
    public static String generateToken(Authentication authentication)
    {
        // Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String jwt = Jwts.builder()
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime() + 86400000))
                    .claim("email", authentication.getName())
                    .signWith(key, SignatureAlgorithm.HS384)
                    .compact();
        return jwt;
    }

    public static String getEmailFromJwtToken(String jwt)
    {
        jwt = jwt.substring(7);
        System.out.println("jwt: " + jwt);
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        String email = String.valueOf(claims.get("email"));
        return email;
    }
}
