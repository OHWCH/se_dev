package com.user.firstproject.domain.user.repository;

//package com.user.firstproject.domain.user.repository;

import com.user.firstproject.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsById(Long id);

    Optional<UserEntity> findByGithubId(String githubId);

    Optional<UserEntity> findBySupabaseId(String supabaseId);
}



