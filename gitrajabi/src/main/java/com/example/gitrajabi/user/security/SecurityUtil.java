package com.example.gitrajabi.user.security;

import com.example.gitrajabi.user.security.JwtTokenProvider.CustomJwtUserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {

    private SecurityUtil() {
    }

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomJwtUserPrincipal jwtUser) {
            return jwtUser.getUserId();
        }

        // 다른 타입의 principal (예: "anonymousUser")인 경우
        return null;
    }
}