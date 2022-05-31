package com.server.amething.domain.user.service;

import com.server.amething.domain.user.User;
import com.server.amething.domain.user.dto.ProFileDto;
import com.server.amething.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Test
    @DisplayName("프로필 구현을 위한 정보를 가져오는 로직")
    void loadProFile() {
        //given
        ProFileDto proFileDto = new ProFileDto("user","img");
        userRepository.save(User.builder()
                        .userName(proFileDto.getUserName())
                        .profilePicture(proFileDto.getProfilePicture())
                        .build());
        //when
        ProFileDto user = userService.loadProFile(proFileDto.getUserName());

        //then
        assertThat(user.getUserName()).isEqualTo(proFileDto.getUserName());
        assertThat(user.getProfilePicture()).isEqualTo(proFileDto.getProfilePicture());
    }
}