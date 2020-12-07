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

/**
 * Security configuration class protecting against external requests
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;


    private static final String WEBSOCKET_CONNECT_ENDPOINT = "/greeting/**";
    private static final String WEBSOCKET_SEND = "/api/v1/app/**";
    private static final String MANAGER_ENDPOINT = "/api/v1/manager/**";
    private static final String LOGIN_ENDPOINT = "/api/v1/auth/login";
    private static final String USER_ENDPOINT = "/api/v1/users/**";
    private static final String CHAT_ENDPOINT = "/api/v1/chat/**";
    private static final String REVIEW = "/api/v1/review/**";
    private static final String ISSUE = "/api/v1/issue/**";
    private static final String CLIENT = "/api/v1/client/**";
    private static final String LAWYER = "/api/v1/lawyer/**";
    private static final String APPEAL = "/api/v1/appeal/**";
    private static final String ALL_ADMIN = "/api/v1/admin/**";
    private static final String INDEX = "/api/v1/admin/index";
    private static final String COMMON_ENDPOINT = "/api/v1/common/**";
    private static final String SETTING = "/api/v1/setting/**";
    private static final String ALL_CSS = "/css/**";
    private static final String ALL_JS = "/js/**";
    private static final String ALL_IMG = "/img/**";
    private static final String TOPIC = "/topic/**";
    private static final String SOCKET_APP = "/app/**";
    private static final String ALL = "/**";



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
                .antMatchers(ALL_JS).permitAll()
                .antMatchers(LOGIN_ENDPOINT).permitAll()
                .antMatchers(COMMON_ENDPOINT).permitAll()
                .antMatchers(ALL_CSS).permitAll()
                .antMatchers(ALL_IMG).permitAll()
                .antMatchers(CHAT_ENDPOINT).permitAll()
                .antMatchers(INDEX).permitAll()
                .antMatchers(TOPIC).permitAll()
                .antMatchers(WEBSOCKET_SEND).permitAll()
                .antMatchers(SOCKET_APP).permitAll()
                .antMatchers(ALL).permitAll()
                .antMatchers(WEBSOCKET_CONNECT_ENDPOINT).permitAll()
                .antMatchers(APPEAL).hasAnyRole("CLIENT", "ADMIN")
                .antMatchers(LAWYER).hasAnyRole("LAWYER", "ADMIN")
                .antMatchers(ISSUE).hasAnyRole("ADMIN")
                .antMatchers(REVIEW).hasAnyRole("CLIENT", "LAWYER", "ADMIN")
                .antMatchers(CLIENT).hasAnyRole("CLIENT", "ADMIN")
                .antMatchers(ALL_ADMIN).hasRole("ADMIN")
                .antMatchers(SETTING).hasAnyRole("CLIENT", "LAWYER", "ADMIN")
                .antMatchers(USER_ENDPOINT).hasAnyRole("CLIENT", "LAWYER", "ADMIN")
                .antMatchers(MANAGER_ENDPOINT).hasRole("MANAGER")
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}
