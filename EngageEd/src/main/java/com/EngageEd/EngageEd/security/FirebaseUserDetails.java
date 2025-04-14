package com.EngageEd.EngageEd.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

@Getter
public class FirebaseUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;
    
    private final String uid;
    private final String email;
    private final String displayName;
    private final Collection<? extends GrantedAuthority> authorities;
    
    public FirebaseUserDetails(String uid, String email, String displayName, 
            Collection<? extends GrantedAuthority> authorities) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        // Not applicable for Firebase authentication
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}