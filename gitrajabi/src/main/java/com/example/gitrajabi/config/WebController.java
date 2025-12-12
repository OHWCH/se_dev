package com.example.gitrajabi.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    // 리액트(프론트엔드)의 페이지 경로들을 모두 이곳에 등록합니다.
    // 이 주소로 요청이 오면, 스프링 부트는 에러 대신 index.html을 반환합니다.
    @GetMapping(value = {
            "/login",
            "/logintest",
            "/callback",
            "/community/**",
            "/studylist/**",
            "/study/**",
            "/mypage/**",
            "/postwrite/**",
            "/challenge/**"
    })

    public String forwardCallback() {
        return "forward:/index.html";
    }

    // 혹시 다른 리액트 페이지(예: /profile 등)도 새로고침 시 에러나면 여기에 추가하면 됩니다.
    // @GetMapping({"/profile", "/study/**"})
    // public String forwardReactPaths() { return "forward:/index.html"; }
}