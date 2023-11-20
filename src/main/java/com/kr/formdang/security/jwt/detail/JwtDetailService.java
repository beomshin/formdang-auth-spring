package com.kr.formdang.security.jwt.detail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class JwtDetailService implements UserDetailsService {

//    private final RootUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // ToDo 캐시 적용 여부 확인
        log.debug("[JwtDetailService] JWT 토큰 인증 유저 정보 조회 : userId [{}] =============>", userId);
//        return new UserAdapter(userRepository.findByUserId(Long.parseLong(userId)).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저 입니다.")));
        return null;
    }
}
