package com.user.firstproject.global.security.user;

//package com.user.firstproject.global.security.user;

import com.user.firstproject.domain.user.entity.UserEntity;
import com.user.firstproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /** 이메일 기반 로딩 (이메일 로그인용, 필요시 사용) */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return new CustomUserDetails(user);
    }

    /** JWT 토큰의 userId 기반 로딩 */
    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));

        return new CustomUserDetails(user);
    }
}


