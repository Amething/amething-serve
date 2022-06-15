package com.server.amething.domain.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.amething.domain.auth.dto.TokenResponseDto;
import com.server.amething.domain.auth.dto.UserProfileResponseDto;
import com.server.amething.domain.auth.service.OauthService;
import com.server.amething.domain.user.User;
import com.server.amething.domain.user.enum_type.Role;
import com.server.amething.domain.user.service.UserService;
import com.server.amething.global.response.ResponseService;
import com.server.amething.global.response.result.SingleResult;
import com.server.amething.global.util.UserUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;
    private final UserService userService;
    private final ResponseService responseService;

    @GetMapping("/code")
    private SingleResult<Map<String, String>> login(@RequestParam String code) throws JsonProcessingException {
        TokenResponseDto token = oauthService.getAccessToken(code);
        UserProfileResponseDto userProfile = oauthService.getUserProfile(token.getAccess_token());

        return responseService.getSingleResult(userService.login(userProfile, Collections.singletonList(Role.ROLE_MEMBER)));
    }

}
