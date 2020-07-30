package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Optional;


@RestController
public class HelloController {

    @Autowired
    CustomMapReactiveUserDetailsService customMapReactiveUserDetailsService;

    @GetMapping("/public")
    public Mono<String> notSecured(Principal principal) {
        return Mono.just("Hello " + name(principal) + ", from a public page");
    }

    @GetMapping("/secured")
    public Mono<String> secured(Principal principal) {
        return Mono.just("Hello " + name(principal) + ", from a secured page");
    }

    @GetMapping("/sign-up/{newUserName}")
    public Mono<String> signUp(@PathVariable("newUserName") String userName, ServerWebExchange exchange) {
        UserDetails newUser = User.withDefaultPasswordEncoder()
                .username(userName)
                .password("password")
                .roles("USER")
                .build();
        customMapReactiveUserDetailsService.addNewUser(newUser);

        return exchange.getSession()
                .doOnNext(session -> {
                    SecurityContextImpl securityContext = new SecurityContextImpl();
                    UsernamePasswordAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(userName, null, newUser.getAuthorities());
                    securityContext.setAuthentication(authentication);
                    session.getAttributes()
                        .put(WebSessionServerSecurityContextRepository.DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME, securityContext);
                })
                .flatMap(WebSession::changeSessionId)
                .then(Mono.just("You are signed up and logged in. You can go to /secured without login"));
    }

    private String name(Principal principal) {
        return Optional.ofNullable(principal).map(Principal::getName).orElse("anonymous");
    }
}
