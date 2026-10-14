package com.project.ProjectManagement.security;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class Config {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            // stateless session(server does not store any info like session id)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 
            // any request starting with /api will need to be authenticated and rest will be permitted without auth
            .authorizeHttpRequests(request -> request.requestMatchers("/api/**")
                .authenticated()
                .anyRequest().permitAll())
            // add a jwt auth filter before the original filter
            .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
            .csrf(csrf -> csrf.disable()) // disable the csrf token
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // enable frontend to access the backend api(bean for corsConfigurationSource defined below)
            .build();
    }


    // bean for cors configuration
    @Bean
    CorsConfigurationSource corsConfigurationSource()
    {
        return new CorsConfigurationSource() {

            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest arg0) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // allow all requests from React(vite) to backend
                config.setAllowedMethods(Collections.singletonList("*")); // allow all methods get post delete patch etc
                config.setAllowCredentials(true); // allow the client to send credentials (like cookies or HTTP auth)
                config.setAllowedHeaders(Collections.singletonList("*")); // allow the client to send headers to the server
                config.setExposedHeaders(Arrays.asList("Authorization")); // allow the SERVER to send the Authorization header(JWT token) to the client
                config.setMaxAge(3600L); // Whenever a request for auth is made, browser caches it for 3600 seconds i.e. 1 hour, if not set, then browser will each time send the request when asked to the server instead of storing it to the caches
                return config;
            }         
        };
    }

    @Bean
    PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
