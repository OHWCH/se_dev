package com.example.gitrajabi.IssueManagement.dto;

import com.example.gitrajabi.IssueManagement.domain.ChallengeType;

/**
 * (DTO) 프론트엔드 '도전과제 리스트' 화면에 뿌려줄 데이터입니다.
 * - 제목, 설명 (카드 내용)
 * - 현재 횟수 / 목표 횟수 (프로그레스 바 용도)
 * - 달성 여부 (카드 색상 변경 또는 체크 표시 용도)
 */
public record ChallengeResponseDto(
        Long challengeId,
        String title,        // 예: "커밋 100회 완료"
        String description,  // 예: "총 100개의 커밋을 완료하세요."
        int currentCount,    // 예: 45 (현재 나의 커밋 수)
        int goal,            // 예: 100 (목표)
        boolean isCompleted  // 달성 여부 (true/false)
) {}