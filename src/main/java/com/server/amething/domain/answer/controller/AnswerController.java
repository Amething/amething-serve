package com.server.amething.domain.answer.controller;

import com.server.amething.domain.answer.dto.RegistrationAnswerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AnswerController {

    @PostMapping("/questionId}/answer")
    private void registrationAnswer(@PathVariable Long questionId, @RequestBody RegistrationAnswerDto registrationAnswerDto) {

    }
}
