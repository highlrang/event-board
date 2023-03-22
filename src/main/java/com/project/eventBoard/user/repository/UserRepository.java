package com.project.eventBoard.user.repository;

import com.project.eventBoard.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsById(String userId);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
