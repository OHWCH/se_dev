package com.user.firstproject.domain.user.dto;

//package com.user.firstproject.domain.user.dto;

//package com.user.firstproject.domain.user.dto;

import com.user.firstproject.domain.user.entity.Role;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private boolean githubLinked;
    private String profileImg;
    private String bio;
    private Role role;
    private Instant createdAt;
}
