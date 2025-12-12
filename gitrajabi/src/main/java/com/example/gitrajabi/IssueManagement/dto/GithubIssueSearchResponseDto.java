package com.example.gitrajabi.IssueManagement.dto;

import java.util.List;

/**
 * (엔티티) GitHub 이슈 검색 API ('/search/issues')의
 * 최상위 응답 구조를 담는 DTO입니다.
 * (간결한 구조 원칙 준수)
 *
 * [주석] API가 반환하는 전체 JSON 구조 { "total_count": 3, "items": [...] } 중에서
 * 우리는 'items' 배열만 필요하므로, 이 구조에 맞게 DTO를 설계합니다.
 * (올바른 기능 수행 원칙 준수)
 */
public record GithubIssueSearchResponseDto(
        /**
         * 실제 이슈 객체들이 담긴 리스트
         */
        List<GithubIssueDto> items
) {}