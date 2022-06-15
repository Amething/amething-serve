package com.server.amething.global.jwt;

import com.server.amething.domain.user.enum_type.Role;
import com.server.amething.global.jwt.config.CustomUserDetails;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
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
    private final String REFRESH_HEADER = "RefreshToken";
    private final String PREFIX = "Bearer ";

    private final CustomUserDetails customUserDetails;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createAccessToken(String oauthId, List<Role> roles) {
        Claims claims = Jwts.claims().setSubject(oauthId);
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
        UserDetails userDetails = customUserDetails.loadUserByUsername(getOauthId(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        Date expirationTime = parseClaims(token).getExpiration();
        return expirationTime.after(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String getOauthId(String token) {
        return parseClaims(token).getSubject();
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(REFRESH_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(PREFIX)) {
            return bearerToken.substring(7);
        }

        return null;
    }

}
