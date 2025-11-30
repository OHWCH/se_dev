package com.example.gitrajabi.user.security;

import com.example.gitrajabi.user.domain.entity.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * ERD 기반 CustomUserDetails
 * - username : github_id
 * - admin 여부 : is_admin
 * - 활성 상태 : deleted_at null 여부
 */
@Getter
public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final String githubId;
    private final boolean admin;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean accountNonLocked;
    private final boolean accountNonExpired;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    public CustomUserDetails(Long userId,
                             String githubId,
                             boolean admin,
                             Collection<? extends GrantedAuthority> authorities,
                             boolean accountNonLocked,
                             boolean accountNonExpired,
                             boolean credentialsNonExpired,
                             boolean enabled) {
        this.userId = userId;
        this.githubId = githubId;
        this.admin = admin;
        this.authorities = authorities;
        this.accountNonLocked = accountNonLocked;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    /**
     * ERD 기준 UserEntity -> CustomUserDetails 변환
     */
    public static CustomUserDetails from(UserEntity user) {
        String role = user.isAdmin() ? "ROLE_ADMIN" : "ROLE_USER";

        return new CustomUserDetails(
                user.getUserId(),
                user.getGithubId(),
                user.isAdmin(),
                List.of(new SimpleGrantedAuthority(role)),
                true,
                true,
                true,
                !user.isDeleted()
        );
    }

    // === UserDetails 구현부 === //

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * username 으로 github_id 사용
     */
    @Override
    public String getUsername() {
        return githubId;
    }

    /**
     * 비밀번호 기반 로그인을 사용하지 않으므로 null 리턴 가능
     * (폼 로그인 안 쓰고 JWT/OAuth만 쓴다고 가정)
     */
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
