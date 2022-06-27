package com.server.amething.domain.user.service;

import com.server.amething.domain.user.User;
import com.server.amething.domain.user.dto.ChangeBioDto;
import com.server.amething.domain.user.dto.ProfileDto;
import com.server.amething.domain.user.enum_type.Role;
import com.server.amething.domain.user.repository.UserRepository;
import com.server.amething.global.jwt.config.CustomUserDetails;
import com.server.amething.global.util.UserUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    UserUtil userUtil;
    @Autowired
    CustomUserDetails customUserDetails;


    @BeforeEach
    @DisplayName("로그인 되어있는 유저를 확인하는 테스트")
    void currentMember() {
        //given
        User user = User.builder()
                .bio("")
                .nickname("김태민")
                .oauthId(2249049917L)
                .profilePicture("")
                .roles(Collections.singletonList(Role.ROLE_MEMBER))
                .refreshToken("Bearer refreshToken")
                .build();

        userRepository.save(user);

        // when login session 발급
        UserDetails userDetails = customUserDetails.loadUserByUsername(user.getUsername());

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @Test
    @DisplayName("로그아웃이 잘 되나요?")
    void logoutTest() {
        userService.logout();
        assertEquals(null, userUtil.getCurrentUser().getRefreshToken());
    }

    @Test
    @DisplayName("프로필 구현을 위한 정보를 가져오는 로직")
    void loadProfile() {
        //given
        User user = userUtil.getCurrentUser();
        //when
        ProfileDto profile = userService.loadProfile(user.getOauthId());

        //then
        assertThat(user.getNickname()).isEqualTo(profile.getUserName());
        assertThat(user.getProfilePicture()).isEqualTo(profile.getProfilePicture());
    }

    @Test
    @DisplayName("bio가 잘 바뀌나요?")
    void changeBioTest() {
        //given when
        User currentUser = userUtil.getCurrentUser();
        String beforeBio = currentUser.getBio();

        userService.changeBio(new ChangeBioDto("나는야 김태민"));

        //then
        Assertions.assertThat("나는야 김태민").isEqualTo(currentUser.getBio());
        assertNotEquals(currentUser.getBio(), beforeBio);
    }
}