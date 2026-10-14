package com.project.ProjectManagement.security;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;


import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Validates the JWT token
public class JwtTokenValidator extends OncePerRequestFilter{
    public static final SecretKey secretKey = Keys.hmacShaKeyFor("wenjndewnjenwjfnwn3iroqiejpqn;p3jedmajioaeiojfoifewjaceqlmn".getBytes());
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                
        if (request.getRequestURI().startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = request.getHeader("Authorization"); // get the JWT header containing the token
        // JWT header FORMAT: Bearer <token>

        if (jwt != null) 
        {
            jwt = jwt.substring(7); // get the JWT token
            System.out.println("jwt: " + jwt);
            try {
                // Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();, demonstrates the process of parsing and validating a JSON Web Token (JWT) using the JJWT library in Java.
                // Jwts.parserBuilder():
                    // This initiates the creation of a JwtParserBuilder instance.
                // .setSigningKey(key):
                    // This method sets the secret key used to verify the digital signature of the JWT. The key variable represents the secret key, which can be a SecretKey object (e.g., generated using Keys.hmacShaKeyFor()) or a byte array. This step is crucial for ensuring the token's authenticity and integrity, as it verifies that the token has not been tampered with.
                // .build():
                    // After configuring the parser with the signing key (and potentially other settings like clock skew tolerance), build() is called to create an immutable JwtParser instance.
                // .parseClaimsJws(jwt):
                    // This is the core parsing and validation step.
                    // parseClaimsJws() is used when the JWT is expected to be a JWS (JSON Web Signature), meaning it has a digital signature.
                    // It takes the jwt string as input, which is the actual JSON Web Token to be parsed.
                    // During this process, the library verifies the signature using the provided key. If the signature is invalid or the token has been altered, a SignatureException or other parsing exceptions will be thrown.
                    // If the validation is successful, it returns a Jws<Claims> object, which encapsulates the header, payload (claims), and signature of the JWS.
                // .getBody():
                    // Finally, getBody() is called on the Jws<Claims> object to extract the Claims object. The Claims object is essentially a Map<String, Object> containing the payload of the JWT, which includes standard claims (like iss, sub, exp, iat) and any custom claims defined in the token.
                Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwt).getBody();

                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));

                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                
                // generate the actual authentication token from the credentials carried as payload in the JWT token
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                throw new BadCredentialsException("Invalid token");
            }
        }
        
        // go the next filter in the filter chain
        filterChain.doFilter(request, response);
        return;
    }

}
