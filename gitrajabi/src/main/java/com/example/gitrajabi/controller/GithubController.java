package com.example.gitrajabi.controller;

import com.example.gitrajabi.github.GithubService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubController {

    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    // 예시: http://localhost:8080/github/profile?user=사용자이름
    @GetMapping("/github/profile")
    public String getProfile(@RequestParam("user") String username) {

        // GithubService를 호출하여 프로필 정보를 JSON 문자열로 받습니다.
        String profileJson = githubService.getUserProfile(username);

        // 결과를 화면에 표시합니다.
        return profileJson;
    }
}