package com.server.amething.domain.user.service;

import com.server.amething.domain.auth.dto.UserProfileResponseDto;
import com.server.amething.domain.user.enum_type.Role;

import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String, String> login(UserProfileResponseDto userProfileResponseDto, List<Role> roles);
}
