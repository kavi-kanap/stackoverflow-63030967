package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {
    

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers("/secured").hasRole("USER")
                .anyExchange().permitAll()
                .and().httpBasic()
                .and().build();
    }

    @Bean
    public CustomMapReactiveUserDetailsService userDetailsService() {
        // There are no registered users at this point
        Map<String,  UserDetails> users = new HashMap<>();
        return new CustomMapReactiveUserDetailsService(users);
    }
}
