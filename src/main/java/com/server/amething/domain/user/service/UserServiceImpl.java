package com.server.amething.domain.user.service;

import com.server.amething.domain.auth.dto.UserProfileResponseDto;
import com.server.amething.domain.user.User;
import com.server.amething.domain.user.dto.ProFileDto;
import com.server.amething.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public void saveUserInfo(UserProfileResponseDto userProfileResponseDto) {
        userRepository.save(User.builder()
                .userName(userProfileResponseDto.getProperties().getNickname())
                .profilePicture(userProfileResponseDto.getProperties().getProfile_image())
                .build()
        );
    }

    @Override
    public ProFileDto loadProFile(String userName) {
        User user = userRepository.findAllByUserName(userName);
        ProFileDto proFileDto = new ProFileDto(user.getUserName(),user.getProfilePicture());
        return proFileDto;
    }
}
