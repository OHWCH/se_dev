package com.user.firstproject.domain.user.dto;

//package com.user.firstproject.domain.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GithubProfileDto {
    private String githubId;
    private String nickname;
    private String avatarUrl;
    private String bio;
}

