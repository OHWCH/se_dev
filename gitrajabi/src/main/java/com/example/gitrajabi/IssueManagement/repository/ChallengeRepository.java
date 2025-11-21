package com.example.gitrajabi.IssueManagement.repository;

import com.example.gitrajabi.IssueManagement.domain.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    // 필요한 경우 추가 메소드 정의
}