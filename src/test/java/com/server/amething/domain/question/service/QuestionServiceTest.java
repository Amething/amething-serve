package com.server.amething.domain.question.service;

import com.server.amething.domain.answer.Answer;
import com.server.amething.domain.answer.repository.AnswerRepository;
import com.server.amething.domain.question.Question;
import com.server.amething.domain.question.dto.QuestionAndAnswerDto;
import com.server.amething.domain.question.dto.QuestionDto;
import com.server.amething.domain.question.enum_type.QuestionType;
import com.server.amething.domain.question.repository.QuestionRepository;
import com.server.amething.domain.user.User;
import com.server.amething.domain.user.enum_type.Role;
import com.server.amething.domain.user.repository.UserRepository;
import com.server.amething.global.jwt.config.CustomUserDetails;
import com.server.amething.global.util.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class QuestionServiceTest {

    @Autowired
    QuestionService questionService;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    UserUtil userUtil;
    @Autowired
    CustomUserDetails customUserDetails;

    @BeforeEach
    @DisplayName("질문 기능을 테스트 하기 위해 User 데이터 생성")
    void saveUser() {
        //given
        User user = User.builder()
                .bio("")
                .nickname("김태민")
                .oauthId(2249049917L)
                .profilePicture("")
                .roles(Collections.singletonList(Role.ROLE_MEMBER))
                .refreshToken("Bearer refreshToken")
                .build();
        userRepository.save(user);

        // when login session 발급
        UserDetails userDetails = customUserDetails.loadUserByUsername(user.getUsername());

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @Test
    @DisplayName("질문 저장 로직")
    void createQuestion() {
        //given
        User user = userUtil.getCurrentUser();

        List<QuestionDto> questions;
        QuestionDto questionDto = new QuestionDto("개발자를 시작하시게 된 경위가 무엇인가요?");
        //when
        questionService.createQuestion(user.getOauthId(),questionDto);
        questions = questionRepository.findUnReplyDescriptionByUser(user);
        //then
        assertEquals(questions.get(0).getTitle(), "개발자를 시작하시게 된 경위가 무엇인가요?");
    }

    @Test
    @DisplayName("저장된 미답변 질문을 가져오는 로직")
    void findUnReplyQuestion() {
        //given
        User user = userUtil.getCurrentUser();
        List<QuestionDto> questions;
        Question question = Question.builder()
                .id(1L)
                .user(user)
                .type(QuestionType.PIN) //해당 컬럼의 type에 따라 출력 여부가 달라짐.
                .title("개발자를 시작하시게 된 경위가 무엇인가요?")
                .build();
        QuestionDto questionDto2 = new QuestionDto("백엔드 시작하시게 된 경위가 무엇인가요?");
        QuestionDto questionDto3 = new QuestionDto("학교에서 무엇을 배우나요??");


        //when
        questionRepository.save(question);
        questionService.createQuestion(user.getOauthId(),questionDto2);
        questionService.createQuestion(user.getOauthId(),questionDto3);
        questions = questionRepository.findUnReplyDescriptionByUser(user);
        //then
        questions.forEach(questionDto -> System.out.println(questionDto.getTitle()));
    }

    @Test
    void findPinQuestion() {
        //given
        User user = userUtil.getCurrentUser();
        List<QuestionAndAnswerDto> questions;

        Question question = Question.builder()
                .id(1L)
                .user(user)
                .type(QuestionType.PIN) //해당 컬럼의 type에 따라 출력 여부가 달라짐.
                .title("개발자를 시작하시게 된 경위가 무엇인가요?")
                .build();
        Answer answer = Answer.builder()
                .id(1L)
                .question(question)
                .description("고등학교에 입학하면서 시작하게 되었습니다!")
                .build();

        //when
        questionRepository.save(question);
        answerRepository.save(answer);
        questions = questionRepository.findPinDescriptionByUser(user);

        //then
        assertEquals(questions.size(),1);
        questions.forEach(questionAndAnswerDto -> System.out.println(questionAndAnswerDto.getDescription() +" / "+ questionAndAnswerDto.getTitle()));
    }
}