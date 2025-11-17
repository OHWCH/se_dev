package com.example.gitrajabi.IssueManagement.dto;

/**
 * (엔티티) GitHub GraphQL API v4에 보낼 요청 본문(query)을
 * 직렬화하기 위한 DTO입니다.
 * (간결한 구조 원칙 준수)
 */
public record GraphQLRequestDto(
        String query
) {}