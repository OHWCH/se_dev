package com.example.gitrajabi.test_user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByGithubId(String githubId);
    Optional<User> findById(Long id);
}