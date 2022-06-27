package com.server.amething.global.util;

import com.server.amething.domain.user.User;
import com.server.amething.domain.user.enum_type.Role;
import com.server.amething.domain.user.repository.UserRepository;
import com.server.amething.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenerateMemberUtil {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @PostConstruct
    private void generateMember() {
        String refreshToken = "Bearer " + jwtTokenProvider.createRefreshToken();

        User kimtaemin = constructor("김태민", 2249049915L, refreshToken);
        User jeongyongwoo = constructor("정용우", 2249049916L, refreshToken);

        String taeminAccessToken = jwtTokenProvider.createAccessToken(kimtaemin.getUsername(), kimtaemin.getRoles());
        String yongwooAccessToken = jwtTokenProvider.createAccessToken(jeongyongwoo.getUsername(), jeongyongwoo.getRoles());

        log.info("[================== generateMember =================]");
        log.info("김태민의 accessToken = {}", "Bearer " + taeminAccessToken);
        log.info("정용우의 accessToken = {}", "Bearer " + yongwooAccessToken);
        log.info("공용 refreshToken = {}", refreshToken);
        log.info("[===================== finish ======================]");

    }

    private User constructor(String nickname, Long oauthId, String refreshToken) {
        return userRepository.save(User.builder()
                .nickname(nickname)
                .oauthId(oauthId)
                .bio(nickname +"의 한줄소개")
                .profilePicture("http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg")
                .roles(Collections.singletonList(Role.ROLE_MEMBER))
                .refreshToken(refreshToken)
                .build());
    }
}
