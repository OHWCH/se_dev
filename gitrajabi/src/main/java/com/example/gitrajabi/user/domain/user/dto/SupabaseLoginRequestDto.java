package com.user.firstproject.domain.user.dto;

//package com.user.firstproject.domain.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupabaseLoginRequestDto {

    // Supabase auth.users.id (UUID)
    private String supabaseId;

    // GitHub username (예: user_metadata.user_name)
    private String githubId;

    // 이메일 (Supabase 사용자 이메일)
    private String email;

    // 닉네임 (원하면 user_metadata.name, user_name 등)
    private String nickname;

    // 프로필 이미지 (GitHub avatar_url)
    private String profileImg;

    // (옵션) Supabase access token - 서버에서 검증하고 싶으면 같이 보냄
    private String supabaseAccessToken;
}

