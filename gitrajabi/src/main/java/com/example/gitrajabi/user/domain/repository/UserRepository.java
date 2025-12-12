package com.example.gitrajabi.user.domain.repository;

import com.example.gitrajabi.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByGithubId(String githubId);

    boolean existsByGithubId(String githubId);

    List<UserEntity> findAllByUserIdIn(List<Long> userIds);
}