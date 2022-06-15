package com.server.amething.domain.question.service;

import com.server.amething.domain.question.Question;
import com.server.amething.domain.question.enum_type.QuestionType;
import com.server.amething.domain.question.repository.QuestionRepository;
import com.server.amething.domain.user.User;
import com.server.amething.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    @Override
    public void createQuestion(Long oauthId, String description) {
        Optional<User> user = userRepository.findByOauthId(oauthId);
        Question question = Question.builder()
                .user(user.get())
                .description(description)
                .type(QuestionType.UNREPLY)
                .build();
        questionRepository.save(question);
    }
}
