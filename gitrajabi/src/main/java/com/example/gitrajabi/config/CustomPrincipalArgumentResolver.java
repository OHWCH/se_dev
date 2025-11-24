package com.example.gitrajabi.config;

import com.example.gitrajabi.user.domain.User;
import com.example.gitrajabi.user.repository.UserRepository;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CustomPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;

    public CustomPrincipalArgumentResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 파라미터가 User.class 타입이면서 @AuthenticationPrincipal이 붙어있는 경우 true 반환
        return parameter.hasParameterAnnotation(org.springframework.security.core.annotation.AuthenticationPrincipal.class)
                && parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User oAuth2User) {

            // GitHub ID (id 속성)를 추출
            String githubId = String.valueOf(oAuth2User.getAttributes().get("id"));

            // DB에서 해당 User 엔티티를 찾아 반환
            return userRepository.findByGithubId(githubId).orElse(null);
        }

        return null;
    }
}