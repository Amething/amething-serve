package com.server.amething.domain.user.repository;

import com.server.amething.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findAllByUserName(String userName);
}
