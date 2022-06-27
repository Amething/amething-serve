package com.server.amething.domain.answer.service;

import com.server.amething.domain.answer.dto.RegistrationAnswerDto;

public interface AnswerService {
    void registrationAnswer(Long questionId, RegistrationAnswerDto registrationAnswerDto);
}
