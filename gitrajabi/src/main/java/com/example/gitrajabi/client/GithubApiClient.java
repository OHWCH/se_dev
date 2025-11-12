package com.example.gitrajabi.client;

// 'GithubIssueDto' 대신 'GithubIssueSearchResponseDto'를 import 합니다.
import com.example.gitrajabi.dto.GithubIssueSearchResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GithubApiClient {

    private final WebClient webClient;

    public GithubApiClient(WebClient.Builder githubWebClientBuilder) {
        this.webClient = githubWebClientBuilder.build();
    }

    /**
     * (기능 27) 'good first issue' 라벨이 붙은 이슈를 검색합니다.
     * @param keyword 사용자가 입력한 검색어 (예: "java", "react")
     * @return 검색 결과의 최상위 응답 객체 (Mono 비동기)
     */
    public Mono<GithubIssueSearchResponseDto> searchGoodFirstIssues(String keyword) {
        String query = "label:\"good first issue\" state:open " + keyword;

        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/issues")
                        .queryParam("q", query)
                        .build())
                .retrieve()
                // 반환 타입을 DTO 배열이 아닌, 응답 전체를 감싸는 DTO로 변경합니다.
                .bodyToMono(GithubIssueSearchResponseDto.class);
    }

    // (기능 28: 기여도 측정을 위한 GraphQL API 호출 메소드도 여기에 추가됩니다.)
}