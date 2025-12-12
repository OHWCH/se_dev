package com.example.gitrajabi.IssueManagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * (엔티티) GitHub GraphQL API v4 응답을
 * 역직렬화하기 위한 DTO입니다.
 * (간결한 구조, 올바른 기능 수행 원칙 준수)
 *
 * [주석] 코드의 의도 (가독성):
 * 이 DTO 구조는 우리가 요청할 GraphQL 쿼리 구조와
 * 정확히 일치해야 합니다.
 * '@JsonIgnoreProperties'는 우리가 정의하지 않은
 * 다른 많은 필드들을 무시하여 오류를 방지합니다. (관리성)
 *
 * 쿼리 예시:
 * {
 * "data": {
 * "user": {
 * "contributionsCollection": {
 * "contributionCalendar": {
 * "totalContributions": 100 // (이것이 커밋+@ 횟수)
 * },
 * "totalPullRequestContributions": 10,
 * "totalIssueContributions": 5
 * }
 * }
 * }
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GraphQLResponseDto(
        Data data
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(
            User user
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record User(
            ContributionsCollection contributionsCollection
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ContributionsCollection(
            ContributionCalendar contributionCalendar,
            int totalPullRequestContributions,
            int totalIssueContributions
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ContributionCalendar(
            int totalContributions // [주석] GitHub GraphQL의 'totalContributions'는
            // 커밋, PR 생성, 이슈 생성, 리뷰 등을 *모두* 합친 값입니다.
            // 이 값을 기반으로 순수 커밋 횟수를 추정합니다.
    ) {}
}