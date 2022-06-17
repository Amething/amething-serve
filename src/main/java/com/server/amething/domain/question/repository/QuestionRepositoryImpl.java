package com.server.amething.domain.question.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.amething.domain.question.dto.QuestionAndAnswerDto;
import com.server.amething.domain.question.dto.QuestionDto;
import com.server.amething.domain.question.enum_type.QuestionType;
import com.server.amething.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.server.amething.domain.answer.QAnswer.answer;
import static com.server.amething.domain.question.QQuestion.question;

@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepositoryCustom{

    private final JPAQueryFactory queryFactory;


    @Override
    @Transactional(readOnly = true)
    public Optional<List<QuestionDto>> findUnreplyDescriptionByUser(User user) {
        return Optional.ofNullable(queryFactory.from(question)
                .select(Projections.constructor(QuestionDto.class,
                        question.description
                ))
                .where(question.user.eq(user).and(question.type.eq(QuestionType.UNREPLY)))
                .fetch());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<List<QuestionAndAnswerDto>> findPinDescriptionByUser(User user) {
        return Optional.ofNullable(queryFactory.from(answer)
                .join(answer.question, question)
                .select(Projections.constructor(QuestionAndAnswerDto.class,
                        question.description,
                        answer.title
                ))
                .where(question.user.eq(user).and(question.type.eq(QuestionType.PIN)))
                .fetch());
    }
}
