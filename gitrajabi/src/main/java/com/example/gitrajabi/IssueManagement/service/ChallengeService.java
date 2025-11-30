package com.example.gitrajabi.IssueManagement.service;

import com.example.gitrajabi.IssueManagement.domain.*;
import com.example.gitrajabi.IssueManagement.repository.*;
import com.example.gitrajabi.IssueManagement.dto.ContributionStatsDto; // 횟수 정보 DTO
import com.example.gitrajabi.IssueManagement.dto.ChallengeResponseDto;
import com.example.gitrajabi.user.domain.entity.UserEntity;
import com.example.gitrajabi.user.domain.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChallengeService {

    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;

    public ChallengeService(UserChallengeRepository userChallengeRepository,
                            ChallengeRepository challengeRepository,
                            UserRepository userRepository) {
        this.userChallengeRepository = userChallengeRepository;
        this.challengeRepository = challengeRepository;
        this.userRepository = userRepository;
    }


    /**
     * [추가된 메소드] 도전과제 목록과 진행 상황을 조회하여 반환합니다.
     */
    @Transactional(readOnly = true)
    public List<ChallengeResponseDto> getChallengeList(Long userId) {
        // 1. 유저 정보 조회 (현재 커밋/PR/이슈 횟수를 알기 위해)
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));

        // 2. 모든 도전과제 메타데이터 조회
        List<Challenge> challenges = challengeRepository.findAll();

        // 3. 엔티티 -> DTO 변환
        return challenges.stream().map(challenge -> {

            // 해당 유저가 이 도전과제를 완료했는지 DB에서 확인
            boolean isCompleted = userChallengeRepository
                    .existsByUserIdAndChallengeAndIsCompletedTrue(userId, challenge);

            // 현재 도전과제 타입에 맞는 유저의 활동 횟수 가져오기 (예: 커밋 관련이면 commitCount)
            int currentCount = getCurrentCountForChallenge(user, challenge.getName());

            return new ChallengeResponseDto(
                    challenge.getChallengeId(),
                    challenge.getName().getTitle(), // [주의] ChallengeType 에 getTitle() 메소드가 필요합니다.
                    challenge.getDescription(),
                    currentCount,
                    challenge.getGoal(),
                    isCompleted
            );
        }).collect(Collectors.toList());
    }

    // [내부 헬퍼 메소드] 도전과제 타입에 따라 현재 유저의 어떤 카운트를 쓸지 결정
    private int getCurrentCountForChallenge(UserEntity user, ChallengeType type) {
        String typeName = type.name();
        if (typeName.startsWith("COMMIT")) return user.getCommitCount();
        if (typeName.startsWith("PR")) return user.getPrCount();
        if (typeName.startsWith("ISSUE")) return user.getIssueCount();
        return 0;
    }

    /**
     * (컨트롤) 사용자의 현재 기여도(stats)를 바탕으로
     * 모든 도전과제의 달성 여부를 확인하고 업데이트합니다.
     *
     * [주석] 코드의 목적 (자동화된 달성 체크):
     * GitHub API에서 최신 횟수를 가져올 때마다 이 메소드를 호출하여,
     * 조건(예: 커밋 5회)을 달성했는지 검사하고 DB에 '완료' 처리를 합니다.
     */
    public void updateChallengeStatus(Long userId, ContributionStatsDto stats) {
        // 1. 모든 도전과제 정의를 가져옵니다.
        List<Challenge> allChallenges = challengeRepository.findAll();

        for (Challenge challenge : allChallenges) {
            // 2. 이미 달성한 도전과제는 건너뜁니다. (최적화)
            if (userChallengeRepository.existsByUserIdAndChallengeAndIsCompletedTrue(userId, challenge)) {
                continue;
            }

            // 3. 도전과제 타입별로 달성 조건을 체크합니다.
            if (checkCondition(challenge, stats)) {
                // 4. 조건 달성 시 완료 처리
                completeChallenge(userId, challenge);
            }
        }
    }

    /**
     * (내부 로직) 각 도전과제의 목표 횟수(Goal)와 현재 횟수를 비교합니다.
     */
    private boolean checkCondition(Challenge challenge, ContributionStatsDto stats) {
        int currentCount = 0;

        // 도전과제 타입 이름(문자열)에 따라 어떤 횟수를 비교할지 결정
        String typeName = challenge.getName().name();

        if (typeName.startsWith("COMMIT")) {
            currentCount = stats.commitCount();
        } else if (typeName.startsWith("PR")) {
            currentCount = stats.prCount();
        } else if (typeName.startsWith("ISSUE")) {
            currentCount = stats.issueCount();
        }

        // 목표(Goal) 이상이면 달성!
        return currentCount >= challenge.getGoal();
    }

    private void completeChallenge(Long userId, Challenge challenge) {
        UserChallenge userChallenge = userChallengeRepository.findByUserIdAndChallenge(userId, challenge)
                .orElse(UserChallenge.builder()
                        .userId(userId)
                        .challenge(challenge)
                        .build());

        userChallenge.complete(); // 완료 상태 및 시간 설정
        userChallengeRepository.save(userChallenge);

        // TODO: "축하합니다! 도전과제 달성" 알림 전송 로직을 여기에 추가 가능
    }
}