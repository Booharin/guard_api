package com.api.lawyer.config;

import com.api.lawyer.security.jwt.JwtConfigurer;
import com.api.lawyer.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String MANAGER_ENDPOINT = "/api/v1/manager/**";
    private static final String LOGIN_ENDPOINT = "/api/v1/auth/login";
    private static final String USER_ENDPOINT = "/api/v1/users/**";
    private static final String ALL_SAVE = "/api/v1/review/**";
    private static final String CLIENT = "/api/v1/client/**";
    private static final String ALL_LAWYER = "/api/v1/lawyer/**";
    private static final String ALL_APPEAL = "/api/v1/appeal/**";
    private static final String COMMON_ENDPOINT = "/api/v1/common/**";


    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(ALL_LAWYER).permitAll()
                .antMatchers(ALL_SAVE).permitAll()
                .antMatchers(CLIENT).permitAll()
                .antMatchers(ALL_APPEAL).permitAll()
                .antMatchers(LOGIN_ENDPOINT).permitAll()
                .antMatchers(COMMON_ENDPOINT).permitAll()
                .antMatchers(USER_ENDPOINT).permitAll() // TODO: только для пользователей поменять
                .antMatchers(MANAGER_ENDPOINT).hasRole("MANAGER")
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}
