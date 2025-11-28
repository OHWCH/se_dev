package com.user.user_login.common.security;
/*
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    // getCurrentUserId() 한줄로 userId 받을수 있게 해줌
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        //  principal 타입이 JwtTokenProvider.CustomJwtUserPrincipal 인지 확인
        if (principal instanceof JwtTokenProvider.CustomJwtUserPrincipal jwtUser) {
            return jwtUser.getUserId();
        }

        return null;
    }
} */

import com.user.user_login.common.security.JwtTokenProvider.CustomJwtUserPrincipal;
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


