package com.user.user_login.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
//GitHub 인증 콜백 또는 로그인을 처리할 때 사용
public class GithubAuthDto {
    private String code;        // 인증 코드
    private String state;       // 상태 값
    private String accessToken; // 액세스 토큰
}

