package com.translator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.translator.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}