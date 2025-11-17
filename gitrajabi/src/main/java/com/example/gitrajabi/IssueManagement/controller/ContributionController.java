package com.example.gitrajabi.IssueManagement.controller;

import com.example.gitrajabi.IssueManagement.dto.MyContributionResponseDto;
import com.example.gitrajabi.IssueManagement.service.ContributionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * (바운더리) (기능 28, 29)
 * '마이페이지'의 기여도 통계 및 뱃지 요청을 처리합니다.
 */
@RestController
@RequestMapping("/api/my") // 마이페이지 전용 API 경로 (가독성, 관리성)
public class ContributionController {

    private final ContributionService contributionService;
    private final String devGithubUsername; // 테스트용 GitHub ID

    /**
     * [주석] (변경 탄력성):
     * 현재는 로그인 기능이 없으므로, 테스트할 GitHub ID를
     * PAT 토큰과 마찬가지로 application.properties에서 읽어옵니다.
     * (하드코딩 금지, 관리성 원칙 준수)
     *
     * [미래]
     * 로그인 기능이 완성되면, 이 @Value는 제거하고
     * Spring Security의 @AuthenticationPrincipal로부터
     * 실제 로그인한 사용자의 ID를 받아오도록 수정합니다.
     */
    public ContributionController(
            ContributionService contributionService,
            @Value("${github.user.name}") String devGithubUsername
    ) {
        this.contributionService = contributionService;
        this.devGithubUsername = devGithubUsername;
    }

    /**
     * (기능 28, 29) 현재 사용자의 기여도 횟수와 등급 뱃지를 반환합니다.
     *
     * @return 횟수(stats)와 뱃지(badge)가 포함된 DTO (Mono)
     */
    @GetMapping("/contributions")
    public Mono<MyContributionResponseDto> getMyContributions() {

        // (현재) 임시 개발용 ID를 서비스에 전달합니다.
        // (미래) 실제 로그인한 유저 ID를 전달하도록 변경합니다.
        String currentUsername = devGithubUsername;

        return contributionService.getMyContribution(currentUsername);
    }
}