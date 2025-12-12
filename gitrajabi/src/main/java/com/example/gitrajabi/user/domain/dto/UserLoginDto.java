package com.example.gitrajabi.user.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
// 로그인 요청
public class UserLoginDto {
    private String email;
    private String password;
}
