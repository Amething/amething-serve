package com.server.amething.domain.user.service;

import com.server.amething.domain.auth.dto.UserProfileResponseDto;
import com.server.amething.domain.user.dto.ProfileDto;

public interface UserService {
    void saveUserInfo(UserProfileResponseDto userProfileResponseDto);

    ProfileDto loadProfile(String userName);
}
