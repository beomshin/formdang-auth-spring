package com.kr.formdang.controller;

import com.kr.formdang.config.CustomException;
import com.kr.formdang.jwt.JwtService;
import com.kr.formdang.model.*;
import com.kr.formdang.provider.CookieProvider;
import com.kr.formdang.repository.AuthRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final CookieProvider cookieProvider;
    private final AuthRepository authRepository;

    @PostMapping("/issue")
    public ResponseEntity issue(@RequestBody JwtIssueRequest request) {
        try {
            if (request.getAuth_key() == null) throw new CustomException(GlobalCode.NOT_EXIST_AUTH_KEY);
            long exist = authRepository.countBySecret(request.getAuth_key());
            if (exist == 0) throw new CustomException(GlobalCode.NOT_ALLOWED_ACCESS);
            String accessToken = jwtService.generateAccessToken(request.getId(), "/issue");
            Date expiredTime = jwtService.getExpiredTime(accessToken);
            String refreshToken = jwtService.generateRefreshToken(request.getId(),"/issue");
            return ResponseEntity.ok().body(new JwtResponse(accessToken, refreshToken, expiredTime));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(new DefaultResponse(e.getCode()));
        } catch (Throwable e) {
            log.error("[토큰 생성 오류] =============> ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(new DefaultResponse(GlobalCode.FAIL_ISSUE_TOKEN));
        }
    }

    @RequestMapping(value = "/validate", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity validate(HttpServletRequest request) {
        try {
            String jwtToken = jwtService.parseJwt(request);
            if (jwtToken == null || !jwtService.validateToken(jwtToken)) throw new CustomException(GlobalCode.FAIL_VALIDATE_TOKEN);
            return ResponseEntity.ok().body(new DefaultResponse());
        } catch (CustomException e) {
            log.error("[토큰 인증 에러] ===============> ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(new DefaultResponse(GlobalCode.FAIL_VALIDATE_TOKEN));
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException | SignatureException e) {
            log.error("[토큰 생성 에러] ===============> ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(new DefaultResponse(GlobalCode.FAIL_GENERATE_TOKEN));
        } catch (ExpiredJwtException e) {
            log.error("[토큰 만료 에러] ===============> ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(new DefaultResponse(GlobalCode.EXPIRED_JWT_TOKEN));
        } catch (Throwable e) {
            log.error("[시스템 오류] =============> ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(new DefaultResponse(GlobalCode.SYSTEM_ERROR));
        }
    }

    @PostMapping("/reissue")
    public ResponseEntity reissue(@RequestBody JwtIssueRequest request, @CookieValue(value = "refresh-token") String refreshToken) {
        try {
            if (request.getAuth_key() == null) throw new CustomException(GlobalCode.NOT_EXIST_AUTH_KEY);
            long exist = authRepository.countBySecret(request.getAuth_key());
            if (exist == 0) throw new CustomException(GlobalCode.NOT_ALLOWED_ACCESS);
            if (!jwtService.validateToken(refreshToken)) {
                cookieProvider.removeRefreshTokenCookie();
                throw new CustomException(GlobalCode.FAIL_VALIDATE_TOKEN);
            }

            String newAccessToken = jwtService.generateAccessToken(request.getId(), "/reissue");
            Date expiredTime = jwtService.getExpiredTime(newAccessToken);
            return ResponseEntity.ok().body(new RefreshJwtResponse(newAccessToken, expiredTime));
        } catch (CustomException e) {
            log.error("[토큰 인증 에러] ===============> ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(new DefaultResponse(e.getCode()));
        } catch (Throwable e) {
            log.error("[리프레시 토큰 생성 오류] =============> ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(new DefaultResponse(GlobalCode.FAIL_ISSUE_TOKEN));
        }
    }

}
