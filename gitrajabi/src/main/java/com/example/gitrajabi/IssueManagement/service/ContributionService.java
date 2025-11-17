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

    // (바운더리) API 클라이언트를 주입받습니다.
    public ContributionService(GithubApiClient githubApiClient) {
        this.githubApiClient = githubApiClient;
    }

    /**
     * 사용자의 기여도 통계와 등급 뱃지를 조회합니다.
     *
     * [주석] 이 메소드의 목적 (올바른 기능 수행):
     * 1. API 클라이언트를 통해 GitHub에서 원본 데이터를 가져옵니다.
     * 2. 원본 데이터를 '마이페이지' 표시에 필요한 횟수(DTO)로 변환합니다 (기능 28).
     * 3. 횟수를 기반으로 점수와 뱃지를 계산합니다 (기능 29).
     * 4. 횟수와 뱃지를 합친 DTO로 컨트롤러에 반환합니다.
     * (간결한 구조 - 로직을 이 서비스에 집중시킴)
     *
     * @param username (테스트 중에는 PAT 토큰 소유자의 GitHub ID)
     * @return 횟수(stats)와 뱃지(badge)가 포함된 DTO (Mono)
     */
    public Mono<MyContributionResponseDto> getMyContribution(String username) {

        // 1. (바운더리 호출) GitHub API에서 데이터 가져오기
        return githubApiClient.fetchContributionData(username)
                .map(this::transformToStats) // 2. (엔티티 변환) 원본 -> 횟수 DTO
                .map(stats -> {
                    // 3. (컨트롤 로직) 횟수 -> 점수 -> 뱃지 계산
                    int score = calculateScore(stats);
                    Badge badge = calculateBadge(score);
                    // 4. (엔티티 조합) 최종 DTO 생성
                    return new MyContributionResponseDto(stats, badge);
                });
    }

    /**
     * (기능 28) GraphQL 응답 DTO를 UI용 횟수 DTO로 변환합니다.
     * (간결한 구조 - 책임을 분리)
     */
    private ContributionStatsDto transformToStats(GraphQLResponseDto response) {
        // [주석] 의도: GraphQL 응답이 null일 경우를 대비하여 안전하게 추출합니다.
        // (올바른 기능 수행, 관리성 원칙 준수)
        try {
            var collection = response.data().user().contributionsCollection();

            // [주석] GitHub의 'totalContributions'는 커밋 외의 것도 포함하므로,
            // (PR 횟수 + 이슈 횟수)를 빼서 순수 커밋(비슷한 값)을 추정합니다.
            // (비즈니스 로직)
            int total = collection.contributionCalendar().totalContributions();
            int prs = collection.totalPullRequestContributions();
            int issues = collection.totalIssueContributions();
            int commits = total - prs - issues; // 커밋 횟수 추정

            return new ContributionStatsDto(commits, prs, issues);

        } catch (NullPointerException e) {
            // [주석] API 응답 구조가 예상과 다르거나 유저가 없는 경우
            // (버그 수정 및 관리를 위해) 기본값 0을 반환합니다.
            return new ContributionStatsDto(0, 0, 0);
        }
    }

    /**
     * (기능 29) 횟수에 가중치를 부여하여 총 점수를 계산합니다.
     * (컨트롤 - 핵심 비즈니스 로직)
     *
     * [주석] 코드의 목적 (주석이 잘 작성되어 있다):
     * - PR(5점): 코드 리뷰를 통과한 완성도 높은 기여
     * - 이슈(3점): 문제를 발견하고 리포팅한 기여
     * - 커밋(1점): 단순 코드 기여
     * (변경에 탄력적이다 - 가중치가 바뀌면 여기만 수정하면 됨)
     */
    private int calculateScore(ContributionStatsDto stats) {
        // 상수는 대문자로 선언하여 가독성을 높입니다.
        final int PR_WEIGHT = 5;
        final int ISSUE_WEIGHT = 3;
        final int COMMIT_WEIGHT = 1;

        // 가능한 자주 줄바꿈 (가독성 원칙 준수)
        int prScore = stats.prCount() * PR_WEIGHT;
        int issueScore = stats.issueCount() * ISSUE_WEIGHT;
        int commitScore = stats.commitCount() * COMMIT_WEIGHT;

        return prScore + issueScore + commitScore;
    }

    /**
     * (기능 29) 계산된 점수에 맞는 뱃지(등급)를 반환합니다.
     * (컨트롤 - 핵심 비즈니스 로직)
     *
     * [주석] 코드의 목적 (변경 탄력성):
     * - 점수 구간이 변경되면 이 메소드만 수정하면 됩니다.
     */
    private Badge calculateBadge(int score) {
        // 가장 높은 점수부터 간결하게 확인합니다.
        if (score > 1000) {
            return Badge.PLATINUM;
        }
        if (score > 500) {
            return Badge.GOLD;
        }
        if (score > 100) {
            return Badge.SILVER;
        }
        if (score > 0) {
            return Badge.BRONZE;
        }
        return Badge.NONE; // (기본값 0점)
    }
}