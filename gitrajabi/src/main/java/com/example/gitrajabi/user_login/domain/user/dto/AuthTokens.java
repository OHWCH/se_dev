package com.user.user_login.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthTokens {
    private String accessToken;
    private String refreshToken;
}

