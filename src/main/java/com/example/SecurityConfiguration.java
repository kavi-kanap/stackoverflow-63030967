package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        LOGGER.debug("Initializing the security configuration");
        return http.authorizeExchange()
                .pathMatchers("/secured").hasRole("USER")
                .anyExchange().permitAll()
                .and().httpBasic()
                .and().build();
    }

    @Bean
    public CustomMapReactiveUserDetailsService userDetailsService() {
        LOGGER.debug("Initializing the user details service");
        UserDetails user1 = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        Map<String,  UserDetails> users = new HashMap<>();
        users.put(user1.getUsername(), user1);
        return new CustomMapReactiveUserDetailsService(users);
    }
}
