package com.example.gitrajabi.IssueManagement.service;

import com.example.gitrajabi.IssueManagement.client.GithubApiClient;
import com.example.gitrajabi.IssueManagement.domain.Badge;
import com.example.gitrajabi.IssueManagement.domain.UserBaseline;
import com.example.gitrajabi.IssueManagement.dto.ContributionStatsDto;
import com.example.gitrajabi.IssueManagement.dto.MyContributionResponseDto;
import com.example.gitrajabi.IssueManagement.dto.GraphQLResponseDto;
import com.example.gitrajabi.IssueManagement.repository.UserBaselineRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ContributionService {

    private final GithubApiClient githubApiClient;
    private final UserBaselineRepository userBaselineRepository;
    private final ChallengeService challengeService;

    public ContributionService(GithubApiClient githubApiClient,
                               UserBaselineRepository userBaselineRepository,
                               ChallengeService challengeService) {
        this.githubApiClient = githubApiClient;
        this.userBaselineRepository = userBaselineRepository;
        this.challengeService = challengeService;
    }

    /**
     * (기능 28 - 초기화) 회원가입 시점에 딱 한 번 호출됩니다.
     * 현재 GitHub의 횟수를 가져와 DB에 'Baseline'으로 저장합니다.
     */
    public Mono<UserBaseline> initializeBaseline(Long userId, String githubUsername) {
        return githubApiClient.fetchContributionData(githubUsername)
                .map(this::transformToStats)
                .map(stats -> UserBaseline.builder()
                        .userId(userId)
                        .CommitCount(stats.commitCount())
                        .PrCount(stats.prCount())
                        .IssueCount(stats.issueCount())
                        .build())
                .map(userBaselineRepository::save);
    }

    /**
     * (기능 28, 29 - 조회) 마이페이지 조회 시 호출됩니다.
     * (Current - Baseline) 공식을 사용하여 순수 활동량을 계산합니다.
     */
    public Mono<MyContributionResponseDto> getMyContribution(Long userId, String githubUsername) {

        // 1. DB에서 가입 시점의 Baseline 데이터를 조회 (Blocking I/O 처리)
        Mono<UserBaseline> baselineMono = Mono.fromCallable(
                () -> userBaselineRepository.findById(userId)
                        // Baseline이 없으면 0으로 가정 (예외 방지)
                        .orElse(UserBaseline.builder().userId(userId).build())
        ).subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic());

        // 2. GitHub API에서 현재(Current) 데이터를 조회
        Mono<ContributionStatsDto> currentStatsMono = githubApiClient.fetchContributionData(githubUsername)
                .map(this::transformToStats);

        // 3. 두 데이터를 결합하여 계산
        return Mono.zip(currentStatsMono, baselineMono)
                .map(tuple -> {
                    ContributionStatsDto currentStats = tuple.getT1(); // 현재값 (Current)
                    UserBaseline baseline = tuple.getT2();           // 초기값 (Baseline)

                    // [핵심 로직] 순수 증가분 계산 (Current - Baseline)
                    int displayCommits = Math.max(0, currentStats.commitCount() - baseline.getCommitCount());
                    int displayPrs = Math.max(0, currentStats.prCount() - baseline.getPrCount());
                    int displayIssues = Math.max(0, currentStats.issueCount() - baseline.getIssueCount());

                    // 결과 DTO 생성
                    ContributionStatsDto displayStats = new ContributionStatsDto(displayCommits, displayPrs, displayIssues);

                    // 4. 계산된 '순수 증가분'으로 점수 및 뱃지 산정
                    int score = calculateScore(displayStats);
                    Badge badge = calculateBadge(score);

                    // (도전과제 체크도 이 '순수 증가분'을 기준으로 할지, '전체'로 할지 결정 필요)
                    // 여기서는 '순수 증가분'을 기준으로 도전과제를 체크하도록 전달합니다.
                    challengeService.updateChallengeStatus(userId, displayStats);

                    return new MyContributionResponseDto(displayStats, badge);
                });
    }

    private ContributionStatsDto transformToStats(GraphQLResponseDto response) {
        try {
            var collection = response.data().user().contributionsCollection();
            int total = collection.contributionCalendar().totalContributions();
            int prs = collection.totalPullRequestContributions();
            int issues = collection.totalIssueContributions();
            int commits = total - prs - issues;
            if (commits < 0) commits = 0;
            return new ContributionStatsDto(commits, prs, issues);
        } catch (NullPointerException e) {
            return new ContributionStatsDto(0, 0, 0);
        }
    }

    private int calculateScore(ContributionStatsDto stats) {
        final int ISSUE_WEIGHT = 5;
        final int PR_WEIGHT = 3;
        final int COMMIT_WEIGHT = 1;
        return (stats.issueCount() * ISSUE_WEIGHT) +
                (stats.prCount() * PR_WEIGHT) +
                (stats.commitCount() * COMMIT_WEIGHT);
    }

    private Badge calculateBadge(int score) {
        if (score > 1000) return Badge.PLATINUM;
        if (score > 500) return Badge.GOLD;
        if (score > 100) return Badge.SILVER;
        if (score > 0) return Badge.BRONZE;
        return Badge.NONE;
    }
}