package com.example.gitrajabi.service;

import com.example.gitrajabi.client.GithubApiClient;
import com.example.gitrajabi.dto.GithubIssueDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * (컨트롤) 이슈 관련 비즈니스 로직을 처리합니다.
 * (기능 27: 굿퍼스트 이슈 검색)
 */
@Service
public class IssueService {

    private final GithubApiClient githubApiClient;

    // 'GithubApiClient' (바운더리)를 주입받습니다.
    public IssueService(GithubApiClient githubApiClient) {
        this.githubApiClient = githubApiClient;
    }

    /**
     * 'good first issue'를 검색합니다.
     * [주석] 이 메소드의 목적(기능):
     * API 클라이언트(바운더리)로부터 원본 데이터를 받아,
     * UI(프론트엔드)가 사용하기 편한 형태(이슈 리스트)로 가공하여 반환합니다.
     * (간결한 구조, 올바른 기능 수행 원칙 준수)
     *
     * @param keyword 검색 키워드
     * @return 이슈 DTO 리스트 (Mono)
     */
    public Mono<List<GithubIssueDto>> searchGoodFirstIssues(String keyword) {
        return githubApiClient.searchGoodFirstIssues(keyword)
                .map(response -> response.items()); // DTO에서 'items' 리스트만 추출
    }
}