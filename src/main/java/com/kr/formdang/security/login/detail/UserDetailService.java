package com.kr.formdang.security.login.detail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserDetailService implements  UserDetailsService {

//    private final RootUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        log.debug("[UserDetailService] 3. 유저 조회 : loginId [{}] =============>", loginId);
//        UserTb userTb = this.isNormalStatus(userRepository.findByLoginId(loginId));
//        return new UserAdapter(userTb, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        return null;
    }

//    private UserTb isNormalStatus(Optional<UserTb> userTb) throws UsernameNotFoundException {
//        if (!userTb.isPresent()) throw new UsernameNotFoundException("로그인 아이디가 존재하지 않습니다.", new LgException(GlobalCode.NOT_EXIST_USER));
//        else if (userTb.get().getStatus() == Status3Enum.REPORT_STATUS) throw new UsernameNotFoundException("정지된 아이디 입니다.", new LgException(GlobalCode.LOCK_USER));
//        else if (userTb.get().getStatus() == Status3Enum.DELETE_STATUS) throw new UsernameNotFoundException("삭제된 아이디 입니다.", new LgException(GlobalCode.NOT_USE_USER));
//        return userTb.get();
//    }

}
