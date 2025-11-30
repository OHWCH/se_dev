package com.example.gitrajabi.IssueManagement.repository;

import com.example.gitrajabi.IssueManagement.domain.Challenge;
import com.example.gitrajabi.IssueManagement.domain.ChallengeType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    boolean existsByName(ChallengeType name); // 서버 시작 시 중복 데이터 생성 방지용
}