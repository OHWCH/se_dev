package com.example.gitrajabi.IssueManagement.domain;

/**
 * (수정) 현재 구현 가능한 '횟수 기반' 도전과제 목록입니다.
 */
public enum ChallengeType {
    // 커밋 관련
    COMMIT_1("커밋 100회 완료"),
    COMMIT_5("커밋 300회 완료"),
    COMMIT_10("커밋 500회 완료"),

    // PR 관련
    PR_1("PR 10회 완료"),
    PR_5("PR 30회 완료"),
    PR_10("PR 50회 완료"),

    // 이슈 관련
    ISSUE_1("이슈 5회 등록"),
    ISSUE_5("이슈 30회 등록"),
    ISSUE_10("이슈 500회 등록");

    private final String title;

    ChallengeType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}