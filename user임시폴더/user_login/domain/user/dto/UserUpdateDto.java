package com.example.gitrajabi.user_login.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UserUpdateDto {
    private String nickname;
    private String profileImg;
    private String bio;
}

