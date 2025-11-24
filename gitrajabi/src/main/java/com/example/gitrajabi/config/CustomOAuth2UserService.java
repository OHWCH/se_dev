package com.example.gitrajabi.config;

import com.example.gitrajabi.user.domain.User;
import com.example.gitrajabi.user.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 OAuth2UserService를 통해 사용자 정보 가져오기
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // GitHub는 기본적으로 'id'를 Principal Name으로 사용
        String githubId = String.valueOf(oAuth2User.getAttributes().get("id"));
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 1. GitHub 정보 파싱
        String name = (String) attributes.get("login"); // GitHub 로그인 ID를 이름으로 사용
        String email = (String) attributes.get("email"); // 이메일
        String profileImageUrl = (String) attributes.get("avatar_url"); // 프로필 이미지 URL

        // 2. User 엔티티 저장 또는 업데이트
        User user = saveOrUpdate(githubId, name, email, profileImageUrl);

        // 3. Spring Security의 DefaultOAuth2User 객체 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), // 권한 설정
                attributes,
                "id" // GitHub의 사용자 식별 키
        );
    }

    // DB에 사용자 정보를 저장하거나, 이미 존재하면 업데이트
    private User saveOrUpdate(String githubId, String name, String email, String profileImageUrl) {
        User user = userRepository.findByGithubId(githubId)
                .map(entity -> entity.update(name, profileImageUrl))
                .orElse(new User(githubId, name, email, profileImageUrl));

        return userRepository.save(user);
    }
}