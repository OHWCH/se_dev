package com.example.gitrajabi.IssueManagement.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "challenge")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Long challengeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", unique = true)
    private ChallengeType name; // ChallengeType Enum 사용

    private String description;

    private int goal; // 목표 횟수

    // 초기 데이터 세팅을 위한 생성자
    public Challenge(ChallengeType name, String description, int goal) {
        this.name = name;
        this.description = description;
        this.goal = goal;
    }
}