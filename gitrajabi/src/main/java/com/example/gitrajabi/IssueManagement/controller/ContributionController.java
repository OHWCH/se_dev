package com.example.gitrajabi.IssueManagement.controller;

import com.example.gitrajabi.IssueManagement.dto.MyContributionResponseDto;
import com.example.gitrajabi.IssueManagement.service.ContributionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
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
}