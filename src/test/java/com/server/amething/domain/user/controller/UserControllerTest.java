package com.server.amething.domain.user.controller;

import com.server.amething.domain.user.User;
import com.server.amething.domain.user.dto.ProfileDto;
import com.server.amething.domain.user.enum_type.Role;
import com.server.amething.domain.user.repository.UserRepository;
import com.server.amething.domain.user.service.UserService;
import com.server.amething.global.jwt.config.CustomUserDetails;
import com.server.amething.global.util.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserUtil userUtil;

    @Autowired
    CustomUserDetails customUserDetails;

    private MockMvc mvc;

    @BeforeEach
    public void Before(@Autowired UserController userController) {
        mvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @BeforeEach
    @DisplayName("프로필 관련 기능을 테스트 하기 위해 User 데이터 생성")
    void saveUser() {
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
    @DisplayName("프로필 구현을 위한 정보를 가져오는 컨트롤러")
    void loadProfile() throws Exception {
        //given
        User user = userUtil.getCurrentUser();
        //when
        final ResultActions resultActions = mvc.perform(get("/v1/user/{id}", user.getOauthId())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"));
        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk());
    }
}