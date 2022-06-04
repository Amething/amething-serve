package com.server.amething.global.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * resolveToken() 메소드를 HttpRequest 요청에 들어있는 Header정보를 통해 AccessToken을 가져옵니다.
     * AccessToken을 가져왔다면 유효한 토큰인지 JwtTokenProvider의 validateToken()을 통해 검증합니다.
     * 검증이 성공했다면 jwtTokenProvider의 getAuthentication() 메소드를 통해 유저 정보를 꺼내서 SecurityContext에 저장합니다.
     * @param request HttpRequest
     * @param response HttpResponse
     * @param filterChain Filter
     * @author 김태민 - TeMlN
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * HttpReqeust 요청에 포함된 Header 정보를 가지고 AccessToken을 가져옵니다.
     * AccessToken의 토큰타입이 Bearer 타입인지 AccessToken가 Bearer로 시작하는지 검증합니다.
     * 검증에 성공했다면 앞에 "Bearer "이 7글자를 자른뒤 나머지 토큰을 String타입으로 return합니다
     * 만약 Bearer 타입이 아니라면 null을 반환합니다.
     * @param request HttpRequest
     * @return accessToken
     * @author 김태민 - TeMlN
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(PREFIX)) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
