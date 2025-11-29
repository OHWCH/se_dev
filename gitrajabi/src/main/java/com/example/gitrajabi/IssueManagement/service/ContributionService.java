package com.example.gitrajabi.IssueManagement.service;

import com.example.gitrajabi.IssueManagement.client.GithubApiClient;
import com.example.gitrajabi.IssueManagement.domain.Badge;
import com.example.gitrajabi.IssueManagement.dto.ContributionStatsDto;
import com.example.gitrajabi.IssueManagement.dto.MyContributionResponseDto;
import com.example.gitrajabi.IssueManagement.dto.GraphQLResponseDto;
import com.example.gitrajabi.test_user.User;
import com.example.gitrajabi.test_user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class ContributionService {

    private final GithubApiClient githubApiClient;
    private final UserRepository userRepository; // UserBaselineRepository 대신 사용
    private final ChallengeService challengeService;

    public ContributionService(GithubApiClient githubApiClient,
                               UserRepository userRepository,
                               ChallengeService challengeService) {
        this.githubApiClient = githubApiClient;
        this.userRepository = userRepository;
        this.challengeService = challengeService;
    }

    /**
     * [핵심 로직 변경]
     * 1. GitHub API에서 최신 데이터 조회
     * 2. 조회된 값으로 User 테이블 업데이트 (DB 동기화)
     * 3. 총 활동량을 기준으로 점수 및 뱃지 계산
     */
    @Transactional
    public Mono<MyContributionResponseDto> getMyContribution(Long userId, String githubUsername) {
        // 1. GitHub API 호출 (비동기)
        return githubApiClient.fetchContributionData(githubUsername)
                .map(this::transformToStats)
                .flatMap(stats -> {
                    // 2. DB 업데이트 (Blocking I/O 처리)
                    return Mono.fromCallable(() -> {
                        updateUserStatsInDb(userId, stats);
                        return stats;
                    }).subscribeOn(Schedulers.boundedElastic());
                })
                .map(stats -> {
                    // 3. 점수 및 뱃지 계산 (Baseline 차감 로직 삭제됨)
                    int score = calculateScore(stats);
                    Badge badge = calculateBadge(score);

                    // 4. 도전과제 업데이트
                    challengeService.updateChallengeStatus(userId, stats);

                    return new MyContributionResponseDto(stats, badge);
                });
    }

    // [Helper] DB의 User 정보를 최신 횟수로 업데이트
    private void updateUserStatsInDb(Long userId, ContributionStatsDto stats) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. id=" + userId));

        // Lombok @Setter를 사용하여 값 갱신
        user.setCommitCount(stats.commitCount());
        user.setPrCount(stats.prCount());
        user.setIssueCount(stats.issueCount());

        userRepository.save(user);
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
        if (score >= 30000) return Badge.RUBY;      // 30,000점 이상
        if (score >= 15000) return Badge.DIAMOND;   // 15,000점 이상
        if (score >= 5000)  return Badge.PLATINUM;  // 5,000점 이상
        if (score >= 2500)  return Badge.GOLD;      // 2,500점 이상
        if (score >= 500)   return Badge.SILVER;    // 500점 이상
        if (score > 0)      return Badge.BRONZE;    // 1점 이상
        return Badge.NONE;
    }
}