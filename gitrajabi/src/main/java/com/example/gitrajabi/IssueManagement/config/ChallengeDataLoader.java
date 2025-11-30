package com.example.gitrajabi.IssueManagement.config;

import com.example.gitrajabi.IssueManagement.domain.Challenge;
import com.example.gitrajabi.IssueManagement.domain.ChallengeType;
import com.example.gitrajabi.IssueManagement.repository.ChallengeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 서버 실행 시 'ChallengeType' Enum에 정의된 내용을 바탕으로
 * DB에 기초 데이터(Challenge 엔티티)를 적재합니다.
 */
@Component
public class ChallengeDataLoader implements CommandLineRunner {

    private final ChallengeRepository challengeRepository;

    public ChallengeDataLoader(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
    }

    @Override
    public void run(String... args) {
        // Enum에 정의된 모든 타입을 순회하며 DB에 없으면 생성
        Arrays.stream(ChallengeType.values())
                .forEach(this::createChallengeIfNotExists);
    }

    private void createChallengeIfNotExists(ChallengeType type) {
        // 1. 이미 DB에 존재하면 건너뜀 (중복 방지)
        if (challengeRepository.existsByName(type)) {
            return;
        }

        // 2. 존재하지 않으면 목표 횟수(Goal)와 설명을 설정하여 저장
        // (Enum에는 제목만 있으므로, 여기서 매핑 로직을 수행)
        int goal = getGoalFromType(type);
        String description = getDescriptionFromType(type, goal);

        Challenge challenge = new Challenge(type, description, goal);
        challengeRepository.save(challenge);

        System.out.println("[DataInit] 도전과제 생성 완료: " + type.getTitle());
    }

    // [헬퍼] 각 타입별 목표 횟수 매핑
    private int getGoalFromType(ChallengeType type) {
        return switch (type) {
            case COMMIT_1 -> 100;
            case COMMIT_5 -> 300;
            case COMMIT_10 -> 500;

            case PR_1 -> 10;
            case PR_5 -> 30;
            case PR_10 -> 50;

            case ISSUE_1 -> 5;
            case ISSUE_5 -> 30;
            case ISSUE_10 -> 50;
        };
    }

    // [헬퍼] 설명 텍스트 자동 생성
    private String getDescriptionFromType(ChallengeType type, int goal) {
        String action = switch (type.name().split("_")[0]) { // COMMIT, PR, ISSUE 추출
            case "COMMIT" -> "커밋을";
            case "PR" -> "Pull Request를";
            case "ISSUE" -> "이슈를";
            default -> "활동을";
        };
        return String.format("총 %d번의 %s 완료해보세요!", goal, action);
    }
}