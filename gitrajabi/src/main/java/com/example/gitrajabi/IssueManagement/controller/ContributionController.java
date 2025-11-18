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
@RequestMapping("/api/my")
public class ContributionController {

    private final ContributionService contributionService;
    private final String devGithubUsername; // 테스트용 GitHub ID

    public ContributionController(
            ContributionService contributionService,
            @Value("${github.user.name}") String devGithubUsername
    ) {
        this.contributionService = contributionService;
        this.devGithubUsername = devGithubUsername;
    }

    @GetMapping("/contributions")
    public Mono<MyContributionResponseDto> getMyContributions() {
        // 현재 테스트용 계정(devGithubUsername)의 데이터를 바로 조회
        return contributionService.getMyContribution(devGithubUsername);
    }

}