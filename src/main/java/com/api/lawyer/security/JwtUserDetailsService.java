package com.api.lawyer.security;

import com.api.lawyer.model.User;
import com.api.lawyer.security.jwt.JwtUser;
import com.api.lawyer.security.jwt.JwtUserFactory;
import com.api.lawyer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findUserByUserEmail(email);

        if(user == null)
            throw new UsernameNotFoundException("User with email: " + email + " not found");

        JwtUser jwtUser = JwtUserFactory.create(user);
        log.info("loadUserByUsername -> user with email = {} successfully loaded", email);
        return jwtUser;
    }
}
