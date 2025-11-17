package com.example.gitrajabi.IssueManagement.dto;

import com.example.gitrajabi.IssueManagement.domain.Badge;

/**
 * (엔티티) '마이페이지'에 필요한
 * 모든 기여도 정보(횟수 + 뱃지)를
 * 프론트엔드에 최종적으로 반환하는 DTO입니다.
 * (올바른 기능 수행 원칙 준수)
 */
public record MyContributionResponseDto(
        ContributionStatsDto stats, // (기능 28) 횟수 통계
        Badge badge              // (기능 29) 계산된 뱃지 등급
) {}