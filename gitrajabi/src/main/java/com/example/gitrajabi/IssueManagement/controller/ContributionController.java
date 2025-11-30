package com.example.gitrajabi.IssueManagement.controller;

import com.example.gitrajabi.IssueManagement.dto.MyContributionResponseDto;
import com.example.gitrajabi.IssueManagement.service.ContributionService;
import com.example.gitrajabi.user.security.SecurityUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/my")
public class ContributionController {

    private final ContributionService contributionService;

    public ContributionController(ContributionService contributionService) {
        this.contributionService = contributionService;
    }

    /**
     * 내 기여도 조회 (인증된 사용자 기준)
     */
    @GetMapping("/contributions")
    public Mono<MyContributionResponseDto> getMyContributions() {
        // 1. SecurityContext에서 현재 로그인한 유저 ID 추출
        Long userId = SecurityUtil.getCurrentUserId();

        if (userId == null) {
            return Mono.error(new IllegalStateException("로그인이 필요합니다."));
        }

        // 2. 서비스 호출 (GithubUsername은 서비스 내부에서 DB를 통해 조회하므로 파라미터에서 제거됨)
        return contributionService.getMyContribution(userId);
    }
}