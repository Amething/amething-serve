package com.server.amething.domain.user.controller;

import com.server.amething.domain.user.User;
import com.server.amething.domain.user.dto.ProFileDto;
import com.server.amething.domain.user.repository.UserRepository;
import com.server.amething.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class UserControllerTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    private MockMvc mvc;

    @BeforeEach
    public void Before(@Autowired UserController userController) {
        mvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("프로필 구현을 위한 정보를 가져오는 컨트롤러")
    void loadProFile() throws Exception {
        //given
        ProFileDto proFileDto = new ProFileDto("user","img");
        userRepository.save(User.builder()
                .userName(proFileDto.getUserName())
                .profilePicture(proFileDto.getProfilePicture())
                .build());
        //when
        final ResultActions resultActions = mvc.perform(get("/user/{forumName}", proFileDto.getUserName())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"));
        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk());
    }
}