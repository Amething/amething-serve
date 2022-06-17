package com.server.amething.domain.question.service;

import com.server.amething.domain.question.Question;
import com.server.amething.domain.question.dto.QuestionDto;
import com.server.amething.domain.question.enum_type.QuestionType;
import com.server.amething.domain.question.repository.QuestionRepository;
import com.server.amething.domain.user.User;
import com.server.amething.domain.user.enum_type.Role;
import com.server.amething.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class QuestionServiceTest {

    @Autowired
    QuestionService questionService;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("질문 저장 로직")
    void createQuestion() {
        //given
        User user = User.builder()
                .bio("")
                .nickname("김태민")
                .oauthId(2249049915L)
                .profilePicture("")
                .roles(Collections.singletonList(Role.ROLE_MEMBER))
                .refreshToken("Bearer refreshToken")
                .build();
        userRepository.save(user);
        List<QuestionDto> questions;
        QuestionDto questionDto = new QuestionDto("개발자를 시작하시게 된 경위가 무엇인가요?");
        //when
        questionService.createQuestion(2249049915L,questionDto);
        questions = questionRepository.findUnreplyDescriptionByUser(user)
                .orElseThrow(()-> new IllegalArgumentException("당신의 질문을 확인할 수 없습니다!"));
        //then
        assertEquals(questions.get(0).getDescription(), "개발자를 시작하시게 된 경위가 무엇인가요?");
    }

    @Test
    @DisplayName("저장된 미답변 질문을 가져오는 로직")
    void loadUnreplyQuestion() {
        //given
        User user = User.builder()
                .bio("")
                .nickname("김태민")
                .oauthId(2249049915L)
                .profilePicture("")
                .roles(Collections.singletonList(Role.ROLE_MEMBER))
                .refreshToken("Bearer refreshToken")
                .build();
        userRepository.save(user);
        List<QuestionDto> questions;
        Question question = Question.builder()
                .id(1L)
                .user(user)
                .type(QuestionType.PIN) //해당 컬럼의 type에 따라 출력 여부가 달라짐.
                .description("개발자를 시작하시게 된 경위가 무엇인가요?")
                .build();
        QuestionDto questionDto2 = new QuestionDto("백엔드 시작하시게 된 경위가 무엇인가요?");
        QuestionDto questionDto3 = new QuestionDto("학교에서 무엇을 배우나요??");


        //when
        questionRepository.save(question);
        questionService.createQuestion(2249049915L,questionDto2);
        questionService.createQuestion(2249049915L,questionDto3);
        questions = questionRepository.findUnreplyDescriptionByUser(user)
                .orElseThrow(()-> new IllegalArgumentException("당신의 질문을 확인할 수 없습니다!"));

        //then
        questions.forEach(questionDto -> System.out.println(questionDto.getDescription()));
    }

    @Test
    void loadPinQuestion() {
        //given
        User user = User.builder()
                .bio("")
                .nickname("김태민")
                .oauthId(2249049915L)
                .profilePicture("")
                .roles(Collections.singletonList(Role.ROLE_MEMBER))
                .refreshToken("Bearer refreshToken")
                .build();
        userRepository.save(user);

        List<QuestionDto> questions;

        Question question = Question.builder()
                .id(1L)
                .user(user)
                .type(QuestionType.PIN) //해당 컬럼의 type에 따라 출력 여부가 달라짐.
                .description("개발자를 시작하시게 된 경위가 무엇인가요?")
                .build();
        Question question1 = Question.builder()
                .id(2L)
                .user(user)
                .type(QuestionType.PIN) //해당 컬럼의 type에 따라 출력 여부가 달라짐.
                .description("백엔드 시작하시게 된 경위가 무엇인가요?")
                .build();
        QuestionDto questionDto3 = new QuestionDto("학교에서 무엇을 배우나요??");

        //when
        questionRepository.save(question);
        questionRepository.save(question1);
        questionService.createQuestion(2249049915L,questionDto3);
        questions = questionRepository.findPinDescriptionByUser(user)
                .orElseThrow(()-> new IllegalArgumentException("당신의 질문을 확인할 수 없습니다!"));

        //then
        questions.forEach(questionDto -> System.out.println(questionDto.getDescription()));
    }
}