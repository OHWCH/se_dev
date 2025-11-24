package com.example.gitrajabi.IssueManagement.controller;

import com.example.gitrajabi.IssueManagement.domain.UserBaseline; // [추가]
import com.example.gitrajabi.IssueManagement.dto.MyContributionResponseDto;
import com.example.gitrajabi.IssueManagement.service.ContributionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping; // [추가]
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/my")
public class ContributionController {

    private final ContributionService contributionService;
    private final String devGithubUsername;
    private final Long devUserId;

    public ContributionController(
            ContributionService contributionService,
            @Value("${github.user.name}") String devGithubUsername
    ) {
        this.contributionService = contributionService;
        this.devGithubUsername = devGithubUsername;
        this.devUserId = 1L;
    }

    @GetMapping("/contributions")
    public Mono<MyContributionResponseDto> getMyContributions() {
        return contributionService.getMyContribution(devUserId, devGithubUsername);
    }

    /**
     * [복구] (테스트 전용)
     * 현재 GitHub 상태를 '0점' 기준점(Baseline)으로 설정합니다.
     * 이 API를 호출한 시점부터 기여도가 카운트되기 시작합니다.
     */
    @PostMapping("/contributions/baseline")
    public Mono<UserBaseline> initializeMyBaseline() {
        return contributionService.initializeBaseline(devUserId, devGithubUsername);
    }
}