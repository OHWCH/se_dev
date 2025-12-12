package com.example.gitrajabi.IssueManagement.client;

import com.example.gitrajabi.IssueManagement.dto.GithubIssueSearchResponseDto;
import com.example.gitrajabi.IssueManagement.dto.GraphQLRequestDto;
import com.example.gitrajabi.IssueManagement.dto.GraphQLResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GithubApiClient {

    private final WebClient webClient;

    public GithubApiClient(WebClient.Builder githubWebClientBuilder) {
        this.webClient = githubWebClientBuilder.build();
    }

    // (기능 27: 이전 단계에서 구현한 메소드)
    public Mono<GithubIssueSearchResponseDto> searchGoodFirstIssues(String keyword, int page) {
        // ... (이전과 동일) ...
        String query = "label:\"good first issue\" state:open " + keyword;

        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/issues")
                        .queryParam("q", query)
                        .queryParam("sort", "created")
                        .queryParam("per_page", "10")
                        .queryParam("page", page)
                        .build())
                .retrieve()
                .bodyToMono(GithubIssueSearchResponseDto.class);
    }

    /**
     * (기능 28) GitHub GraphQL API(v4)를 호출하여
     * 사용자의 기여도 통계(커밋, PR, 이슈)를 조회합니다.
     *
     * [주석] 코드의 목적 (올바른 기능 수행):
     * REST API v3는 커밋 횟수를 가져오기 매우 복잡합니다.
     * GraphQL API v4를 사용하면 *단 한 번의 요청*으로
     * 'contributionsCollection'에서 모든 횟수를 가져올 수 있습니다. (효율성)
     * (build.gradle에 'graphql' 의존성이 포함되어 있음)
     *
     * @param username (테스트 중에는 PAT 토큰 소유자의 GitHub ID)
     * @return GraphQL API 응답 DTO (Mono)
     */
    public Mono<GraphQLResponseDto> fetchContributionData(String username) {

        // [주석] GraphQL 쿼리: (가독성)
        // 1. user(login: ...) : 특정 유저를 조회합니다.
        // 2. contributionsCollection : 지난 1년간의 기여도 데이터를 요청합니다.
        // 3. contributionCalendar.totalContributions : 총 커밋 + PR + 이슈 + 리뷰 등
        // 4. totalPullRequestContributions : 총 PR 기여 횟수
        // 5. totalIssueContributions : 총 이슈 생성 횟수
        String query = """
            query {
              user(login: "%s") {
                contributionsCollection {
                  contributionCalendar {
                    totalContributions
                  }
                  totalPullRequestContributions
                  totalIssueContributions
                }
              }
            }
            """.formatted(username); // (가독성을 위해 Java 17 Text Block 사용)

        GraphQLRequestDto requestBody = new GraphQLRequestDto(query);

        /*
         * [주석] '/graphql' 엔드포인트는 GithubApiConfig에 설정된
         * 'baseUrl' (api.github.com)과 합쳐져
         * 'https://api.github.com/graphql'로 호출됩니다.
         * (간결한 구조, 변경 탄력성 원칙 준수)
         */
        return this.webClient.post()
                .uri("/graphql") // GraphQL은 항상 POST와 /graphql 엔드포인트를 사용
                .bodyValue(requestBody)     // 쿼리가 담긴 DTO를 body에 전송
                .retrieve()
                .bodyToMono(GraphQLResponseDto.class);
    }
}