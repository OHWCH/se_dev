package com.user.user_login.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GithubProfileDto {
    private Long id;          // github numeric id
    private String login;     // github username
    private String avatarUrl; // profile image url
    private String bio;       // bio
    private String email;     // email (null일 수도 있음)
}

