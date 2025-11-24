package com.user.firstproject.global.security.user;

//package com.user.firstproject.global.security.user;

import com.user.firstproject.domain.user.entity.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final String username; // email
    private final String password;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(UserEntity user) {
        this.userId = user.getId();
        this.username = user.getEmail();
        this.password = user.getPassword(); // Supabase + GitHub만 쓴다면 null일 수 있음
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // 계정 상태 관련: 기본적으로 true로 허용
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}



