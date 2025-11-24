package com.user.firstproject.domain.user.dto;

//package com.user.firstproject.domain.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDto {
    private String nickname;
    private String profileImg;
    private String bio;
}

