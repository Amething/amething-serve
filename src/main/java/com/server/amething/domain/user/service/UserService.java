package com.server.amething.domain.user.service;

import com.server.amething.domain.auth.dto.UserProfileResponseDto;
import com.server.amething.domain.user.User;
import com.server.amething.domain.user.dto.ProFileDto;

public interface UserService {
    void saveUserInfo(UserProfileResponseDto userProfileResponseDto);
    User loadProFile(String userName);
}
