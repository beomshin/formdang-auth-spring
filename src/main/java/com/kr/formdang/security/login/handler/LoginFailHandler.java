package com.kr.formdang.security.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kr.formdang.config.CustomException;
import com.kr.formdang.model.DefaultResponse;
import com.kr.formdang.model.GlobalCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class LoginFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("[LoginFailHandler] 로그인 실패 =========> ");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), new DefaultResponse(getFailCode(exception)));
    }

    private GlobalCode getFailCode(AuthenticationException exception) {
        if (exception instanceof AuthenticationServiceException) { // 로그인 정보 누락 && 로그인 아이디 미존재
            log.error("[LoginFailHandler] 로그인 정부 누락 / 로그인 아이디 미존재 ==========> ");
            return GlobalCode.NOT_EXIST_USER;
        } else if(exception instanceof BadCredentialsException) { // 비밀번호 불일치 에러
            log.error("[LoginFailHandler] 비밀번호 불일치 ==========> ");
            return GlobalCode.FAIL_LOGIN_INFO;
        } else if (exception.getCause() instanceof CustomException) { // 유저 정보 조회 실패 에러
            CustomException e = (CustomException) exception.getCause();
            log.error("[LoginFailHandler] {} ==========> ", e.getCode().getMsg());
            return e.getCode();
        }
        return GlobalCode.SYSTEM_ERROR;
    }
}
