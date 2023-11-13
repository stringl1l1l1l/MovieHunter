package com.example.config;

import com.example.filter.SMTPAuthenticationProcessingFilter;
import com.example.util.SMTPAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@RequiredArgsConstructor
public class SMTPAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Resource
    private final UserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity http) {

        SMTPAuthenticationProcessingFilter authenticationFilter = new SMTPAuthenticationProcessingFilter();
        authenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
//        authenticationFilter.setAuthenticationSuccessHandler(new SMTPAuthenticationSuccessHandler());
//        authenticationFilter.setAuthenticationFailureHandler(new SMTPAuthenticationFailureHandler());

        SMTPAuthenticationProvider authenticationProvider = new SMTPAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);

        http.authenticationProvider(authenticationProvider)
                .addFilterAfter(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
