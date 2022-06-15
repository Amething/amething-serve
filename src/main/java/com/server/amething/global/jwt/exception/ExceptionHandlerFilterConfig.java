package com.server.amething.global.jwt.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@Configuration
@RequiredArgsConstructor
public class ExceptionHandlerFilterConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final ExceptionHandlerFilter exceptionHandlerFilter;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.addFilterBefore(exceptionHandlerFilter, SecurityContextPersistenceFilter.class);
    }
}
