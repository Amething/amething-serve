package com.server.amething.util;

import com.server.amething.domain.user.User;
import com.server.amething.domain.user.enum_type.Role;
import com.server.amething.domain.user.repository.UserRepository;
import com.server.amething.global.util.UserUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

@SpringBootTest
public class UserUtilTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserUtil userUtil;

    @BeforeEach
    @DisplayName("testing 하기위해 user를 db에 저장 후 SecurityContext에 저장")
    void registerUser() {
        User taemin = User.builder()
                .nickname("김태민")
                .oauthId(1111L)
                .roles(Collections.singletonList(Role.ROLE_MEMBER))
                .build();

        userRepository.save(taemin);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                taemin.getId(),
                "",
                taemin.getRoles());

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);
        System.out.println(securityContext);
    }

    @Test
    @DisplayName("user가 잘 불러와 지나요?")
    void currentUserTest() {
        //given
        User currentUser = userUtil.getCurrentUser();

        //when
        //then
        Assertions.assertThat("김태민").isEqualTo(currentUser.getNickname());
    }
}
