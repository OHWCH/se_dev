package com.example.gitrajabi.IssueManagement.domain;

/**
 * (엔티티) (기능 29) 기여도 점수에 따라 부여되는
 * 뱃지(등급)를 정의하는 Enum입니다.
 * (가독성, 관리성 원칙 준수)
 */
public enum Badge {
    NONE, // 0점
    BRONZE, // 1 ~ 500
    SILVER, // 500 ~ 2500
    GOLD, // 2500 ~ 5000
    PLATINUM, // 5000 ~ 15000
    DIAMOND, // 15000 ~ 30000
    RUBY // 30000 이상
}