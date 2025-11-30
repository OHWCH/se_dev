package com.user.user_login.domain.user.repository;
/*
import com.user.user_login.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//DB 접근만 담당함
// 이메일 중복체크, 로그인 시 이메일로 조회, Github연동 시 githubid로 조회 등 쿼리 처리
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByGithubId(String githubId);

    boolean existsByEmail(String email);

    boolean existsById(Long id);
} */

import com.user.user_login.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByGithubId(String githubId);

    boolean existsByGithubId(String githubId);
}


