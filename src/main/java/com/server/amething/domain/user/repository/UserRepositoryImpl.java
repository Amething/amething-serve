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
    public ProfileDto findProfileByUsername(String username) {
        return queryFactory.from(user)
                .select(Projections.constructor(ProfileDto.class,
                        user.userName,
                        user.profilePicture
                        ))
                .where(user.userName.eq(username))
                .fetchOne();
    }
}
