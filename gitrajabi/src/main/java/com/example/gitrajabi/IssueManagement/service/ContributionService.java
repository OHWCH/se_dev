package com.example.gitrajabi.IssueManagement.service;

import com.example.gitrajabi.IssueManagement.client.GithubApiClient;
import com.example.gitrajabi.IssueManagement.domain.Badge;
import com.example.gitrajabi.IssueManagement.dto.ContributionStatsDto;
import com.example.gitrajabi.IssueManagement.dto.MyContributionResponseDto;
import com.example.gitrajabi.IssueManagement.dto.GraphQLResponseDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * (컨트롤) 사용자의 기여도 측정(28) 및 뱃지(29) 부여
 * 비즈니스 로직을 담당합니다.
 */
@Service
public class ContributionService {

    private final GithubApiClient githubApiClient;

    public ContributionService(GithubApiClient githubApiClient) {
        this.githubApiClient = githubApiClient;
    }

    /**
     * [수정] (기능 28, 29)
     * GitHub API에서 조회한 '현재 총 기여도'를 그대로 사용하여
     * 점수와 뱃지를 계산합니다. (초기화 로직 제거)
     *
     * @param githubUsername 사용자의 GitHub ID
     * @return 현재 총 횟수(stats)와 뱃지(badge) DTO (Mono)
     */
    public Mono<MyContributionResponseDto> getMyContribution(String githubUsername) {

        // 1. API에서 데이터 가져오기
        return githubApiClient.fetchContributionData(githubUsername)
                .map(this::transformToStats) // 2. DTO 변환
                .map(stats -> {
                    // 3. 점수 및 뱃지 계산
                    int score = calculateScore(stats);
                    Badge badge = calculateBadge(score);

                    // 4. 결과 반환
                    return new MyContributionResponseDto(stats, badge);
                });
    }


    private ContributionStatsDto transformToStats(GraphQLResponseDto response) {
        try {
            var collection = response.data().user().contributionsCollection();
            int total = collection.contributionCalendar().totalContributions();
            int prs = collection.totalPullRequestContributions();
            int issues = collection.totalIssueContributions();
            // GitHub의 total은 모든 기여의 합이므로, 커밋 수를 역산
            int commits = total - prs - issues;

            // (방어 코드) 계산 오차로 음수가 나오면 0으로 보정
            if (commits < 0) commits = 0;

            return new ContributionStatsDto(commits, prs, issues);
        } catch (NullPointerException e) {
            return new ContributionStatsDto(0, 0, 0);
        }
    }

    private int calculateScore(ContributionStatsDto stats) {
        // 가중치: Issue(5) > PR(3) > Commit(1)
        final int ISSUE_WEIGHT = 5;
        final int PR_WEIGHT = 3;
        final int COMMIT_WEIGHT = 1;

        int issueScore = stats.issueCount() * ISSUE_WEIGHT;
        int prScore = stats.prCount() * PR_WEIGHT;
        int commitScore = stats.commitCount() * COMMIT_WEIGHT;

        return issueScore + prScore + commitScore;
    }

    private Badge calculateBadge(int score) {
        if (score > 1000) return Badge.PLATINUM;
        if (score > 500) return Badge.GOLD;
        if (score > 100) return Badge.SILVER;
        if (score > 0) return Badge.BRONZE;
        return Badge.NONE;
    }

    // (initializeBaseline 메소드는 삭제)
}