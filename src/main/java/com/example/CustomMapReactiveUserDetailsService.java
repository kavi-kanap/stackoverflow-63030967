package com.example;


import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public class CustomMapReactiveUserDetailsService extends MapReactiveUserDetailsService {

    private final Map<String, UserDetails> users;

    public CustomMapReactiveUserDetailsService(Map<String, UserDetails> users) {
        super(users);
        this.users = users;
    }

    public void addNewUser(UserDetails user){
        users.put(user.getUsername(), user);
    }
}
