package com.server.amething.domain.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.amething.domain.auth.dto.TokenResponseDto;
import com.server.amething.domain.auth.dto.UserProfileResponseDto;
import com.server.amething.domain.auth.service.OauthService;
import com.server.amething.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;
    private final UserService userService;

    @GetMapping("/code")
    private void getAuthorizationCode(@RequestParam String code) throws JsonProcessingException {
        TokenResponseDto token = oauthService.getAccessToken(code);
        UserProfileResponseDto userProfile = oauthService.getUserProfile(token.getAccess_token());
        userService.saveUserInfo(userProfile);
    }
}
