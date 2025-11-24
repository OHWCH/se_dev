package com.example.gitrajabi.user.repository;

import com.example.gitrajabi.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // GitHub ID (Principal Name)를 사용하여 사용자를 조회
    Optional<User> findByGithubId(String githubId);
}