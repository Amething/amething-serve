package com.server.amething.domain.question.controller;

import com.server.amething.domain.question.dto.QuestionDto;
import com.server.amething.domain.question.service.QuestionService;
import com.server.amething.global.response.ResponseService;
import com.server.amething.global.response.result.CommonResult;
import com.server.amething.global.response.result.SingleResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class QuestionController {

    private final ResponseService responseService;
    private final QuestionService questionService;

    @ResponseStatus(HttpStatus.OK )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oauthId", value = "oauth 로그인시 기본 지급되는 Id", required = true, dataType = "Long", paramType = "path"),
    })
    @PostMapping("/user/{oauthId}/question")
    public CommonResult createQuestion(@PathVariable Long oauthId, @RequestBody QuestionDto questionDto){
        questionService.createQuestion(oauthId, questionDto);
        return responseService.getSuccessResult();
    }

    @ResponseStatus(HttpStatus.OK )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
    })
    @GetMapping("/user/me/questions")
    public SingleResult<List> loadUnreplyQuestion(){
        List<QuestionDto> questions = questionService.loadUnreplyQuestion();
        return responseService.getSingleResult(questions);
    }
}
