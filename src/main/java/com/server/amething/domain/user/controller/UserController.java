package com.server.amething.domain.user.controller;

import com.server.amething.domain.user.dto.ProfileDto;
import com.server.amething.domain.user.service.UserService;
import com.server.amething.global.response.ResponseService;
import com.server.amething.global.response.result.SingleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    @GetMapping("/user/{oauthId}")
    public SingleResult<ProfileDto> loadProfile(@PathVariable Long oauthId){
        ProfileDto profileDto = userService.loadProfile(oauthId);
        return responseService.getSingleResult(profileDto);
    }
}
