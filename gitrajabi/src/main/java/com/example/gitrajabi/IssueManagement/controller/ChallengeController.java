package com.example.gitrajabi.IssueManagement.controller;

import com.example.gitrajabi.IssueManagement.dto.ChallengeResponseDto;
import com.example.gitrajabi.IssueManagement.service.ChallengeService;
import com.example.gitrajabi.user.security.SecurityUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    /**
     * 도전과제 목록 및 진행 상황 조회
     * GET /api/challenges
     */
    @GetMapping
    public List<ChallengeResponseDto> getAllChallenges() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) throw new IllegalStateException("로그인이 필요합니다.");

        return challengeService.getChallengeList(userId);
    }
}