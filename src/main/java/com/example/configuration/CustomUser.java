package com.example.configuration;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


public class CustomUser extends User {

    private Long userID;

    public CustomUser(Long userID, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userID = userID;
    }

    public Long getUserID(){
        return userID;
    }
}
