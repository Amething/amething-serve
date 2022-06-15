package com.server.amething.domain.user.repository;

import com.server.amething.domain.user.dto.ProfileDto;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<ProfileDto> findProfileByOauthId(Long oauthId);
}
