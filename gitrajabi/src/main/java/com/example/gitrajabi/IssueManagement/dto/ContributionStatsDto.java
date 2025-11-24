package com.example.gitrajabi.IssueManagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * (엔티티) (기능 28) 사용자의 기여 횟수 통계를 담는 DTO입니다.
 * '마이페이지.html'에 표시될 최종 데이터입니다.
 * (가독성, 간결한 구조 원칙 준수)
 */
public record ContributionStatsDto(
        @JsonProperty("commit_count") // JSON 키 이름을 스네이크 케이스로 (관리성)
        int commitCount,

        @JsonProperty("pr_count")
        int prCount,

        @JsonProperty("issue_count")
        int issueCount
) {}