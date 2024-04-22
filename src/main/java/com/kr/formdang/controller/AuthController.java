package com.kr.formdang.controller;

import com.kr.formdang.config.FormException;
import com.kr.formdang.model.*;
import com.kr.formdang.provider.CookieProvider;
import com.kr.formdang.provider.JwtTokenProvider;
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
import javax.validation.Valid;
import java.util.Date;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthRepository authRepository;

    @PostMapping("/issue")
    public ResponseEntity<RootResponse> issue(@RequestBody JwtIssueRequest request) {

        try {

            log.info("[요청값] {}", request);
            if (request.getAuth_key() == null) throw new FormException(ResultCode.NOT_EXIST_AUTH_KEY);

            long exist = authRepository.countBySecret(request.getAuth_key());
            if (exist == 0) throw new FormException(ResultCode.NOT_ALLOWED_ACCESS);

            String accessToken = JwtTokenProvider.generateAccessToken(request, "/issue");
            Date expiredTime = JwtTokenProvider.getExpiredTime(accessToken);
            String refreshToken = JwtTokenProvider.generateRefreshToken(request);

            return ResponseEntity.ok().body(new JwtResponse(accessToken, refreshToken, expiredTime));

        } catch (FormException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DefaultResponse(e.getCode()));
        } catch (Throwable e) {

            log.error("[토큰 생성 오류] =============> ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DefaultResponse(ResultCode.FAIL_ISSUE_TOKEN));
        }

    }

    @RequestMapping(value = "/validate", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<RootResponse> validate(HttpServletRequest request) {

        try {

            String jwtToken = JwtTokenProvider.parseJwt(request.getHeader("Authorization"));

            if (jwtToken == null || JwtTokenProvider.validateToken(jwtToken)) throw new FormException(ResultCode.FAIL_VALIDATE_TOKEN);

            return ResponseEntity.ok().body(new DefaultResponse());
        } catch (FormException e) {

            log.error("[토큰 인증 에러] ===============> ");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DefaultResponse(ResultCode.FAIL_VALIDATE_TOKEN));
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException | SignatureException e) {

            log.error("[토큰 생성 에러] ===============> ");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DefaultResponse(ResultCode.FAIL_GENERATE_TOKEN));
        } catch (ExpiredJwtException e) {

            log.error("[토큰 만료 에러] ===============> ");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DefaultResponse(ResultCode.EXPIRED_JWT_TOKEN));
        } catch (Throwable e) {

            log.error("[시스템 오류] =============> ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DefaultResponse(ResultCode.SYSTEM_ERROR));
        }

    }

    @PostMapping("/reissue")
    public ResponseEntity<RootResponse> reissue(@RequestBody JwtIssueRequest request, @CookieValue(value = "refresh-token") String refreshToken) {

        try {

            if (request.getAuth_key() == null) throw new FormException(ResultCode.NOT_EXIST_AUTH_KEY);

            long exist = authRepository.countBySecret(request.getAuth_key());
            if (exist == 0) throw new FormException(ResultCode.NOT_ALLOWED_ACCESS);

            if (JwtTokenProvider.validateToken(refreshToken)) {
                CookieProvider.removeRefreshTokenCookie();
                throw new FormException(ResultCode.FAIL_VALIDATE_TOKEN);
            }

            String newAccessToken = JwtTokenProvider.generateAccessToken(request, "/reissue");
            Date expiredTime = JwtTokenProvider.getExpiredTime(newAccessToken);
            return ResponseEntity.ok().body(new RefreshJwtResponse(newAccessToken, expiredTime));
        } catch (FormException e) {

            log.error("[토큰 인증 에러] ===============> ");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DefaultResponse(e.getCode()));
        } catch (Throwable e) {

            log.error("[리프레시 토큰 생성 오류] =============> ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DefaultResponse(ResultCode.FAIL_ISSUE_TOKEN));
        }
    }

}
