package com.example.gitrajabi.IssueManagement.service;

import com.example.gitrajabi.IssueManagement.client.GithubApiClient;
import com.example.gitrajabi.IssueManagement.domain.Badge;
import com.example.gitrajabi.IssueManagement.dto.ContributionStatsDto;
import com.example.gitrajabi.IssueManagement.dto.MyContributionResponseDto;
import com.example.gitrajabi.IssueManagement.dto.GraphQLResponseDto;
import com.example.gitrajabi.user.domain.entity.UserEntity;
import com.example.gitrajabi.user.domain.repository.UserRepository;
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
     * 1. DB에서 사용자 정보(GitHub ID) 조회
     * 2. GitHub API에서 최신 데이터 조회
     * 3. 조회된 값으로 User 테이블 업데이트 (DB 동기화)
     * 4. 총 활동량을 기준으로 점수 및 뱃지 계산
     */
    @Transactional
    public Mono<MyContributionResponseDto> getMyContribution(Long userId) {
        // 1. DB에서 유저 조회 (Blocking -> Non-Blocking 래핑)
        return Mono.fromCallable(() -> userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. id=" + userId)))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(user -> {
                    String githubId = user.getGithubId();

                    // 2. GitHub API 호출
                    return githubApiClient.fetchContributionData(githubId)
                            .map(this::transformToStats)
                            .flatMap(stats -> {
                                // 3. DB 업데이트 (Blocking I/O 처리)
                                return Mono.fromCallable(() -> {
                                    updateUserStatsInDb(user, stats);
                                    return stats;
                                }).subscribeOn(Schedulers.boundedElastic());
                            });
                })
                .map(stats -> {
                    // 4. 점수 및 뱃지 계산
                    int score = calculateScore(stats);
                    Badge badge = calculateBadge(score);

                    // 5. 도전과제 업데이트
                    challengeService.updateChallengeStatus(userId, stats);

                    return new MyContributionResponseDto(stats, badge);
                });
    }

    // [Helper] DB의 User 정보를 최신 횟수로 업데이트
    private void updateUserStatsInDb(UserEntity user, ContributionStatsDto stats) {
        // 도메인 메소드를 사용하여 값 갱신
        user.updateStats(stats.commitCount(), stats.issueCount(), stats.prCount());
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