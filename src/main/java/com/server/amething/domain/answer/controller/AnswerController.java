package com.server.amething.domain.answer.controller;

import com.server.amething.domain.answer.dto.RegistrationAnswerDto;
import com.server.amething.domain.answer.service.AnswerService;
import com.server.amething.global.response.ResponseService;
import com.server.amething.global.response.result.CommonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;
    private final ResponseService responseService;

    @PostMapping("/{questionId}/answer")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "questionId", value = "질문의 id", required = true, dataType = "Long", paramType = "path")
    })
    private CommonResult registrationAnswer(@PathVariable Long questionId, @RequestBody RegistrationAnswerDto registrationAnswerDto) {
        answerService.registrationAnswer(questionId, registrationAnswerDto);
        return responseService.getSuccessResult();
    }
}
