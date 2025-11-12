package com.example.gitrajabi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * (엔티티) GitHub API 응답 중 개별 이슈 정보를 담는 DTO입니다.
 * UI 표시에 필요한 최소한의 데이터(제목, 클릭 시 이동할 URL, 작성자)만 선언합니다.
 * (가독성, 간결한 구조 원칙 준수)
 */
public record GithubIssueDto(
        String title,

        /**
         * [주석] JSON의 'html_url' 필드를 Java의 'htmlUrl' 카멜 케이스로 매핑합니다.
         * (가독성, 관리성 원칙 준수)
         */
        @JsonProperty("html_url")
        String htmlUrl,

        GithubUserDto user
) {}