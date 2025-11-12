package com.example.gitrajabi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * GitHub API 통신을 위한 설정을 담당합니다.
 * (바운더리 - 외부 API 연동)
 */
@Configuration
public class GithubApiConfig {

    /**
     * application.properties에서 GitHub API 기본 URL을 주입받습니다.
     * (관리성 원칙 준수)
     */
    @Value("${github.api.base-url}")
    private String baseUrl;

    /**
     * application.properties에서 개발용 PAT 토큰을 주입받습니다.
     * (관리성 원칙 준수)
     */
    @Value("${github.api.token}")
    private String devPatToken; // 개발용 토큰 (Dev Personal Access Token)

    /**
     * WebClient 빌더를 Bean으로 등록하여 프로젝트 전역에서 재사용합니다.
     * (간결한 구조 원칙 준수)
     * * @return WebClient.Builder
     */
    @Bean
    public WebClient.Builder githubWebClientBuilder() {

        /*
         * [주석] WebClient의 목적과 탄력적 설계 의도:
         * * 이 WebClient 빌더는 GitHub API와의 통신을 전담합니다.
         * * [현재 (개발 단계)]
         * 개발 및 테스트 편의를 위해 'devPatToken'을 모든 요청의 기본 인증 헤더로 설정합니다.
         * 이렇게 하면 GithubApiClient에서 매번 인증 헤더를 추가할 필요가 없습니다.
         * * [미래 (로그인 기능 완료 시)]
         * 이 .defaultHeader(...) 라인만 삭제하면, WebClient는 인증 헤더가 없는
         * 순수한 상태가 됩니다. (변경에 탄력적인 구조)
         * 그 후 GithubApiClient는 각 메소드에서 실제 로그인한 사용자의 토큰을
         * 파라미터로 받아 동적으로 헤더를 설정하게 됩니다.
         */
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Accept", "application/vnd.github.v3+json")
                .defaultHeader("Authorization", "Bearer " + devPatToken); // <-- 지금은 이 코드로 테스트
    }
}