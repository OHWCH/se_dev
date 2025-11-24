package com.example.gitrajabi.IssueManagement.repository;

import com.example.gitrajabi.IssueManagement.domain.Challenge;
import com.example.gitrajabi.IssueManagement.domain.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {

    // 특정 유저가 특정 챌린지를 이미 완료했는지 확인
    boolean existsByUserIdAndChallengeAndIsCompletedTrue(Long userId, Challenge challenge);

    // 특정 유저의 특정 챌린지 기록 조회
    Optional<UserChallenge> findByUserIdAndChallenge(Long userId, Challenge challenge);
}