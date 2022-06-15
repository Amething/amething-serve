package com.server.amething.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.amething.domain.auth.dto.TokenResponseDto;
import com.server.amething.domain.auth.dto.UserProfileResponseDto;

public interface OauthService {
    TokenResponseDto getAccessToken(String code) throws JsonProcessingException;
    UserProfileResponseDto getUserProfile(String accessToken);
}
