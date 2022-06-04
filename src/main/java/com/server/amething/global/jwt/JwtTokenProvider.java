package com.server.amething.global.jwt;

import com.server.amething.domain.user.enum_type.Role;
import com.server.amething.global.jwt.config.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long ACCESS_TOKEN_EXPIRED_MILLI_SECOND = 1000L * 60 * 60 * 3; // 3시간
    private final long REFRESH_TOKEN_EXPIRED_MILLI_SECOND = 1000L * 60 * 60 * 24 * 30 * 6; // 180일

    private final String AUTHORITIES_KEY = "auth";

    private final CustomUserDetails customUserDetails;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createAccessToken(String kakaoId, List<Role> roles) {
        Claims claims = Jwts.claims().setSubject(kakaoId);
        claims.put(AUTHORITIES_KEY, roles.stream()
                .map(Role::getAuthority)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));

        Date date = new Date();
        Date accessTokenExpiresIn = new Date(date.getTime() + ACCESS_TOKEN_EXPIRED_MILLI_SECOND);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(accessTokenExpiresIn)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

    }

    public String createRefreshToken() {
        Claims claims = Jwts.claims().setSubject(null);

        Date date = new Date();
        Date refreshTokenExpiresIn = new Date(date.getTime() + REFRESH_TOKEN_EXPIRED_MILLI_SECOND);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(refreshTokenExpiresIn)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String accessToken) {
        String oauthId = parseClaims(accessToken).getSubject();

        UserDetails userDetails = customUserDetails.loadUserByUsername(oauthId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        }
         catch (SecurityException | MalformedJwtException e) {
            log.info(" === 잘못된 JWT 서명입니다. === ");
        } catch (ExpiredJwtException e) {
            log.info(" === 만료된 JWT 토큰입니다. === {}");
        } catch (UnsupportedJwtException e) {
            log.info(" === 지원하지 않는 JWT 토큰입니다. === ");
        } catch (IllegalArgumentException e) {
            log.info(" === JWT 토큰이 잘못되었습니다 === ");
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
