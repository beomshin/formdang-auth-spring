package com.kr.formdang.controller;

import com.kr.formdang.config.CustomException;
import com.kr.formdang.jwt.JwtService;
import com.kr.formdang.model.DefaultResponse;
import com.kr.formdang.model.GlobalCode;
import com.kr.formdang.model.JwtResponse;
import com.kr.formdang.model.RefreshJwtResponse;
import com.kr.formdang.provider.CookieProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/issue")
    public ResponseEntity issue() {
        try {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            List<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            String accessToken = jwtService.generateAccessToken("/issue", roles);
            Date expiredTime = jwtService.getExpiredTime(accessToken);
            String refreshToken = jwtService.generateRefreshToken("/issue", roles);
            return ResponseEntity.ok().body(new JwtResponse(accessToken, refreshToken, expiredTime));
        } catch (Throwable e) {
            log.error("[토큰 생성 오류] =============> ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(new DefaultResponse(GlobalCode.FAIL_ISSUE_TOKEN));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity validate(HttpServletRequest request) {
        try {
            String jwtToken = jwtService.parseJwt(request);
            if (jwtToken != null && jwtService.validateToken(jwtToken)) throw new CustomException(GlobalCode.FAIL_VALIDATE_TOKEN);
            return ResponseEntity.ok().body(new DefaultResponse());
        } catch (CustomException e) {
            log.error("[토큰 인증 에러] ===============> ", e);
            return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.FAIL_VALIDATE_TOKEN));
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

    @GetMapping("/reissue")
    public ResponseEntity reissue(@CookieValue(value = "refresh-token") String refreshToken) {
        try {
            if (!jwtService.validateToken(refreshToken)) {
                cookieProvider.removeRefreshTokenCookie();
                throw new CustomException(GlobalCode.FAIL_VALIDATE_TOKEN);
            }

            String newAccessToken = jwtService.generateAccessToken("/reissue", jwtService.getRoles(refreshToken));
            Date expiredTime = jwtService.getExpiredTime(newAccessToken);
            return ResponseEntity.ok().body(new RefreshJwtResponse(newAccessToken, expiredTime));
        } catch (CustomException e) {
            log.error("[토큰 인증 에러] ===============> ", e);
            return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.FAIL_VALIDATE_TOKEN));
        } catch (Throwable e) {
            log.error("[리프레시 토큰 생성 오류] =============> ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(new DefaultResponse(GlobalCode.FAIL_ISSUE_TOKEN));
        }
    }

}
