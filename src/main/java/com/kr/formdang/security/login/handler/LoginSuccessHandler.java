package com.kr.formdang.security.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kr.formdang.jwt.JwtService;
import com.kr.formdang.model.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.debug("[LoginSuccessHandler] 4. 로그인 성공 핸들러 ==========> ");
        String userId = authentication.getName();
        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        String accessToken = jwtService.generateAccessToken(userId, request.getRequestURI(), roles);
        Date expiredTime = jwtService.getExpiredTime(accessToken);
        String refreshToken = jwtService.generateRefreshToken(userId, request.getRequestURI(), roles);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), new LoginResponse(accessToken, refreshToken, expiredTime));
    }

}
