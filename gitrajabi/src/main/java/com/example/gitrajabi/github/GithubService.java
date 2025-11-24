package com.example.gitrajabi.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GithubService {

    private final WebClient webClient;
    private final String githubApiToken;

    public GithubService(WebClient.Builder webClientBuilder,
                         @Value("${github.api.base-url}") String baseUrl,
                         @Value("${github.api.token}") String githubApiToken) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.githubApiToken = githubApiToken;
    }

    /**
     * GitHub API를 통해 특정 사용자의 프로필 정보를 가져옵니다.
     * @param username GitHub 사용자 이름
     * @return GitHub 프로필 정보 (JSON 문자열 형태로 반환)
     */
    public String getUserProfile(String username) {

        String githubResponse = webClient.get()
                .uri("/users/{username}", username)
                .header("Authorization", "token " + githubApiToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return githubResponse;
    }
}