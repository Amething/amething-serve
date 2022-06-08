package com.server.amething.domain.user.repository;

import com.server.amething.domain.user.dto.ProfileDto;

public interface UserRepositoryCustom {
    ProfileDto findProfileByOauthId(Long oauthId);
}
