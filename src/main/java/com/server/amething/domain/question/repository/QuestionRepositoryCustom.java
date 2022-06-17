package com.server.amething.domain.question.repository;

import com.server.amething.domain.question.dto.QuestionDto;
import com.server.amething.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface QuestionRepositoryCustom{
    Optional<List<QuestionDto>> findUnreplyDescriptionByUser(User user);
    Optional<List<QuestionDto>> findPinDescriptionByUser(User user);
}
