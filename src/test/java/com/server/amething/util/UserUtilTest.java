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
    @DisplayName("")
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
    void currentUserTest() {
        User currentUser = userUtil.getCurrentUser();
        Assertions.assertThat("김태민").isEqualTo(currentUser.getNickname());
    }
}
