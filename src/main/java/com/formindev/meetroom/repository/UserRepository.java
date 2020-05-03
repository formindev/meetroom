package com.formindev.meetroom.repository;

import com.formindev.meetroom.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
}
