package com.kr.formdang.provider.security;

import com.kr.formdang.security.login.detail.UserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {

    private final BCryptPasswordEncoder encoder;
    private final UserDetailService userDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("[LoginAuthenticationProvider] 2. 로그인 Provider 진입 ==============> ");

        UserDetails userDetails = userDetailService.loadUserByUsername(authentication.getName());
        String rawPassword = authentication.getCredentials().toString(); // 요청 비밀번호
        String oriPassword = userDetails.getPassword(); // 원본 비밀번호

        if (!encoder.matches(rawPassword, oriPassword)) throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");

        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
