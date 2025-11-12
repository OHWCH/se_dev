package com.example.gitrajabi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * (엔티티) GitHub API 응답 중 사용자 정보를 담는 DTO입니다.
 * '메인화면.html'의 "by @janesmith" 부분을 표시하기 위해 'login' 필드만 선언합니다.
 * (가독성, 간결한 구조 원칙 준수)
 */
public record GithubUserDto(
        String login
) {}