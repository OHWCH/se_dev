package com.user.firstproject.domain.user.dto;

//package com.user.firstproject.domain.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDto {
    private String email;
    private String password;
    private String nickname;
    private String bio;
}

