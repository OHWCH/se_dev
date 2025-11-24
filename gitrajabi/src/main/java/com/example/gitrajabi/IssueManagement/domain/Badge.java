package com.example.gitrajabi.IssueManagement.domain;

/**
 * (엔티티) (기능 29) 기여도 점수에 따라 부여되는
 * 뱃지(등급)를 정의하는 Enum입니다.
 * (가독성, 관리성 원칙 준수)
 */
public enum Badge {
    // 점수 0점 (기본값)
    NONE,
    // 점수 1-100점
    BRONZE,
    // 점수 101-500점
    SILVER,
    // 점수 501-1000점
    GOLD,
    // 점수 1001점 이상
    PLATINUM
}