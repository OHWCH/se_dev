package com.user.firstproject.domain.user.dto;

//package com.user.firstproject.domain.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GithubAuthDto {
    private String code;
    private String state;
    private String accessToken;
}

