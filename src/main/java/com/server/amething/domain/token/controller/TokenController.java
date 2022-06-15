package com.server.amething.domain.token.controller;

import com.server.amething.domain.token.service.TokenService;
import com.server.amething.global.response.ResponseService;
import com.server.amething.global.response.result.SingleResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;
    private final ResponseService responseService;

    @GetMapping("/reissue/access-token")
    @ResponseStatus(HttpStatus.OK)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "RefreshToken", value = "refreshToken", required = true, dataType = "String", paramType = "header"),
    })
    private SingleResult<String> reissueAccessToken(HttpServletRequest request, @RequestParam Long oauthId) {

        return responseService.getSingleResult(tokenService.reissueAccessToken(request, oauthId));
    }

}
