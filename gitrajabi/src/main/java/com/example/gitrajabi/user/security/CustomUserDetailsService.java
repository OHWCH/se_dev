package com.example.gitrajabi.user.security;

import com.example.gitrajabi.user.domain.entity.UserEntity;
import com.example.gitrajabi.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * username 을 github_id로 사용
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByGithubId(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("사용자를 찾을 수 없습니다. github_id=" + username)
                );

        return CustomUserDetails.from(user);
    }
}
