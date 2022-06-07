package com.server.amething.domain.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.amething.domain.user.dto.ProfileDto;
import lombok.RequiredArgsConstructor;

import static com.server.amething.domain.user.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public ProfileDto findProfileByNickname(String nickname) {
        return queryFactory.from(user)
                .select(Projections.constructor(ProfileDto.class,
                        user.nickname,
                        user.profilePicture
                        ))
                .where(user.nickname.eq(nickname))
                .fetchOne();
    }
}
