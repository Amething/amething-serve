package com.server.amething.global.config.security;

import com.server.amething.global.jwt.JwtTokenProvider;
import com.server.amething.global.jwt.config.JwtFilterConfig;
import com.server.amething.global.jwt.exception.ExceptionHandlerFilter;
//import com.server.amething.global.jwt.exception.ExceptionHandlerFilterConfig;
import com.server.amething.global.jwt.exception.ExceptionHandlerFilterConfig;
import com.server.amething.global.jwt.exception.JwtAccessDeniedHandler;
import com.server.amething.global.jwt.exception.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity //(debug = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ExceptionHandlerFilter exceptionHandlerFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .cors().and()
                .csrf().disable()
                .httpBasic().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
//                .antMatchers("/v1/user/**").hasRole("GUEST")
                .anyRequest().permitAll();

        http.exceptionHandling()
                .accessDeniedHandler(new JwtAccessDeniedHandler())
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint());

        http.apply(new JwtFilterConfig(jwtTokenProvider));
        http.apply(new ExceptionHandlerFilterConfig(exceptionHandlerFilter));
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/**") // 후에 Security 설정을 완료하고 제거
                .antMatchers("/h2-console/**", "/exception/**")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/swagger/**")
                .antMatchers("/webjars/**")
                .antMatchers("/configuration/ui")
                .antMatchers("/configuration/security")
                .antMatchers("/context/**");
    }

}