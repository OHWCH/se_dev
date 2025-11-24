package com.example.gitrajabi.IssueManagement.domain;

/**
 * (수정) 현재 구현 가능한 '횟수 기반' 도전과제 목록입니다.
 */
public enum ChallengeType {
    // 커밋 관련
    COMMIT_1("커밋 1회 완료"),
    COMMIT_5("커밋 5회 완료"),
    COMMIT_10("커밋 10회 완료"),

    // PR 관련
    PR_1("PR 1회 완료"),
    PR_5("PR 5회 완료"),
    PR_10("PR 10회 완료"),

    // 이슈 관련
    ISSUE_1("이슈 1회 등록"),
    ISSUE_5("이슈 5회 등록"),
    ISSUE_10("이슈 10회 등록");

    private final String title;

    ChallengeType(String title) {
        this.title = title;
    }
}