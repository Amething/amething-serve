package com.server.amething.domain.answer.service;

import com.server.amething.domain.answer.Answer;
import com.server.amething.domain.answer.dto.RegistrationAnswerDto;
import com.server.amething.domain.answer.repository.AnswerRepository;
import com.server.amething.domain.question.Question;
import com.server.amething.domain.question.enum_type.QuestionType;
import com.server.amething.domain.question.repository.QuestionRepository;
import com.server.amething.domain.user.User;
import com.server.amething.global.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserUtil userUtil;

    @Override
    public void registrationAnswer(Long questionId, RegistrationAnswerDto registrationAnswerDto) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 답변이 존재하지 않습니다."));

        User currentUser = userUtil.getCurrentUser();

        if(!currentUser.getUsername().equals(question.getUser().getUsername())) throw new IllegalArgumentException("답변을 할 권한이 없습니다.");

        Answer answer = Answer.builder()
                .question(question)
                .title(registrationAnswerDto.getTitle())
                .build();

        question.changeQuestionType(QuestionType.PIN);
        answerRepository.save(answer);
    }
}
