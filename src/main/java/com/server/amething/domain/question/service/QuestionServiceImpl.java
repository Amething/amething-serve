package com.server.amething.domain.question.service;

import com.server.amething.domain.question.Question;
import com.server.amething.domain.question.dto.QuestionDto;
import com.server.amething.domain.question.enum_type.QuestionType;
import com.server.amething.domain.question.repository.QuestionRepository;
import com.server.amething.domain.user.User;
import com.server.amething.domain.user.repository.UserRepository;
import com.server.amething.global.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final UserUtil userUtil;

    @Override
    public void createQuestion(Long oauthId, QuestionDto questionDto) {
        User user = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원은 존재하지 않습니다."));
        Question question = Question.builder()
                .user(user)
                .description(questionDto.getDescription())
                .type(QuestionType.UNREPLY)
                .build();
        questionRepository.save(question);
    }

    @Override
    public List<QuestionDto> loadQuestion() {
        User user = userUtil.getCurrentUser();
        return questionRepository.findAllDescriptionByUser(user)
                .orElseThrow(()-> new IllegalArgumentException("당신의 질문을 확인할 수 없습니다!"));
    }
}
