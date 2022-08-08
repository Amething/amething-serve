package com.server.amething.domain.answer.service;

import com.server.amething.domain.answer.dto.RegistrationAnswerDto;
import com.server.amething.domain.answer.repository.AnswerRepository;
import com.server.amething.domain.question.Question;
import com.server.amething.domain.question.enum_type.QuestionType;
import com.server.amething.domain.question.repository.QuestionRepository;
import com.server.amething.domain.user.User;
import com.server.amething.domain.user.enum_type.Role;
import com.server.amething.domain.user.repository.UserRepository;
import com.server.amething.global.util.UserUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AnswerServiceTest {

    @Autowired QuestionRepository questionRepository;
    @Autowired AnswerRepository answerRepository;
    @Autowired UserRepository userRepository;
    @Autowired AnswerService answerService;
    @Autowired UserUtil userUtil;

    @BeforeEach
    void init() {
        User taemin = User.builder()
                .nickname("김태민")
                .oauthId(1111L)
                .roles(Collections.singletonList(Role.ROLE_MEMBER))
                .build();

        userRepository.save(taemin);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                taemin.getId(),
                "",
                taemin.getRoles());

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);
    }

    @Test
    @DisplayName("답변이 잘 등록 되나요?")
    void registrationAnswerTest() {
        User currentUser = userUtil.getCurrentUser();

        Question question = questionRepository.save(Question.builder()
                .id(1L)
                .title("title")
                .type(QuestionType.UNREPLY)
                .user(currentUser)
                .build());

        RegistrationAnswerDto answerDto = new RegistrationAnswerDto("답변입니다.");
        answerService.registrationAnswer(question.getId(), answerDto);

        Assertions.assertThat(question.getType()).isEqualTo(QuestionType.PIN);
    }

    @Test
    @DisplayName("등록이 안된 질문을 불러올 시 예외가 잘 반환되나요?")
    void notApplyQuestionExceptionTest() {
        RegistrationAnswerDto answerDto = new RegistrationAnswerDto("답변입니다.");

        assertThrows(
                IllegalArgumentException.class,
                () -> answerService.registrationAnswer(1L, answerDto));
    }

    @Test
    @DisplayName("자신에게 달린 질문이 아닌 질문에 답변을 할 시 예외가 잘 반환되나요?")
    void notMineQuestionExceptionTest() {
        User user = userRepository.save(User.builder()
                .nickname("다른 사용자")
                .oauthId(1234L)
                .roles(Collections.singletonList(Role.ROLE_MEMBER))
                .build());

        Question question = questionRepository.save(Question.builder()
                .id(1L)
                .title("title")
                .type(QuestionType.UNREPLY)
                .user(user)
                .build());

        RegistrationAnswerDto answerDto = new RegistrationAnswerDto("답변입니다.");

        assertThrows(
                IllegalArgumentException.class,
                () -> answerService.registrationAnswer(question.getId(), answerDto)
        );
    }
}
