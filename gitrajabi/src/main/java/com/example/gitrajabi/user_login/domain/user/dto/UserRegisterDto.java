package com.user.user_login.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
//회원가입 입력 데이터
//회원가입할 때 클라이언트 -> 서버로 들어오는 값이 전부 여기 담김
// 컨트롤러는 이 DTO만 받으면 되고, 내부에서 이걸 UserEntity로 변환해서 저장
public class UserRegisterDto {
    private String email;
    private String password;
    private String nickname;
    private String bio;
}

