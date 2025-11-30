package com.example.gitrajabi.IssueManagement.controller;

import com.example.gitrajabi.IssueManagement.dto.GithubIssueDto;
import com.example.gitrajabi.IssueManagement.service.IssueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * (바운더리) 이슈 관련 HTTP 요청을 처리하는 컨트롤러입니다.
 * 프론트엔드('메인화면.html')와의 연결 지점입니다.
 */
@RestController
@RequestMapping("/api/issues") // API URL 경로 (가독성, 관리성)
public class IssueController {

    private final IssueService issueService;

    // 'IssueService' (컨트롤)를 주입받습니다.
    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    /**
     * (기능 27) 굿퍼스트 이슈 목록을 검색하여 반환합니다.
     * [주석] 이 엔드포인트의 목적(기능):
     * 프론트엔드로부터 'keyword'를 받아 'IssueService'에 전달하고,
     * 서비스가 반환한 이슈 리스트를 JSON 형태로 프론트엔드에 응답합니다.
     * (올바른 기능 수행, 간결한 구조 원칙 준수)
     *
     * @param keyword (예: http://.../api/issues/good-first?keyword=java)
     * @return 이슈 DTO 리스트 (Mono)
     */
    @GetMapping("/good-first")
    public Mono<List<GithubIssueDto>> getGoodFirstIssues(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int page
    ) {
        // 줄바꿈을 통해 인자 구분을 명확하게 합니다. (가독성 원칙 준수)
        return issueService.searchGoodFirstIssues(keyword, page);
    }
}