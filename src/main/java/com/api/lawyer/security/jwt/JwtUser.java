package com.api.lawyer.security.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class JwtUser implements UserDetails {

    private final Integer id;
    private final String email;
    private final String password;
    private final String role;


    public JwtUser(Integer id, String email, String password, String role, boolean isEnable, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isEnable = isEnable;
        this.authorities = authorities;
    }

    private final boolean isEnable;
    private final Collection<? extends GrantedAuthority> authorities;


    public Integer getId() {
        return id;
    }


    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return null;
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

    public String getRole() {
        return role;
    }

    public boolean isEnable() {
        return isEnable;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
