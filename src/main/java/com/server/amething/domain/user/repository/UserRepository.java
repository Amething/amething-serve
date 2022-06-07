package com.server.amething.domain.user.repository;

import com.server.amething.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByOauthId(Long oauthId);
}
