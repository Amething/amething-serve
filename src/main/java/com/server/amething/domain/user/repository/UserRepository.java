package com.server.amething.domain.user.repository;

import com.server.amething.domain.user.User;
import com.server.amething.domain.user.dto.ProFileDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findAllByUserName(String userName);
}
