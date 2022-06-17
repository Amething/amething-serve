package com.server.amething.domain.question.service;

import com.server.amething.domain.question.dto.QuestionDto;

import java.util.List;

public interface QuestionService {
    void createQuestion(Long oauthId, QuestionDto questionDto);
    List<QuestionDto> loadUnreplyQuestion();
}
