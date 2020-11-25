package com.api.lawyer.security.jwt;

import com.api.lawyer.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.ArrayList;
import java.util.List;

public final class JwtUserFactory {

    public JwtUserFactory(){}

    public static JwtUser create(User user){
        return new JwtUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                Boolean.TRUE,
                mapToGrantedAuthorities(user.getRole()));
    }
    
    private static List<GrantedAuthority> mapToGrantedAuthorities(String role){
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(role));
        return grantedAuthorities;
    }
}
