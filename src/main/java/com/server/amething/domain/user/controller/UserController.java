package com.server.amething.domain.user.controller;

import com.server.amething.domain.user.dto.ChangeBioDto;
import com.server.amething.domain.user.dto.ProfileDto;
import com.server.amething.domain.user.service.UserService;
import com.server.amething.global.response.ResponseService;
import com.server.amething.global.response.result.CommonResult;
import com.server.amething.global.response.result.SingleResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    @ResponseStatus(HttpStatus.OK )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "oauthId", value = "oauth 로그인시 기본 지급되는 Id", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("/user/{oauthId}")
    public SingleResult<ProfileDto> loadProfile(@PathVariable Long oauthId){
        ProfileDto profileDto = userService.loadProfile(oauthId);
        return responseService.getSingleResult(profileDto);
    }

    @PutMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    private CommonResult logout() {
        userService.logout();
        return responseService.getSuccessResult();
    }

    @PutMapping("/user/bio")
    @ResponseStatus(HttpStatus.OK)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    private CommonResult changeBio(@RequestBody ChangeBioDto changeBioDto) {
        userService.changeBio(changeBioDto);
        return responseService.getSuccessResult();
    }

}
