package com.server.amething.domain.token.service;

import com.server.amething.domain.user.User;
import com.server.amething.domain.user.repository.UserRepository;
import com.server.amething.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public String reissueAccessToken(HttpServletRequest request, Long oauthId) {
        String requestRefreshToken = jwtTokenProvider.resolveRefreshToken(request);

        User user = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        String userRefreshToken = user.getRefreshToken().substring(7);

        if (userRefreshToken.equals(requestRefreshToken)) {
            String accessToken = jwtTokenProvider.createAccessToken(user.getUsername(), user.getRoles());
            return accessToken;
        } else throw new IllegalArgumentException("refreshToken이 없거나 유효하지 않습니다.");

    }
}