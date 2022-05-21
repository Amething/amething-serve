package com.server.amething.domain.user.service;

import com.server.amething.domain.auth.dto.UserProfileResponseDto;

public interface UserService {
    void saveUserInfo(UserProfileResponseDto userProfileResponseDto);
}
