package com.kr.formdang.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

public class CookieProvider {

    private static String refreshTokenExpiredTime;

    public CookieProvider(
            @Value("${token.refresh-expired-time}") String refresh_token_expired_time
    ) {
        refreshTokenExpiredTime = refresh_token_expired_time;
    }

    public static ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refresh-token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Long.parseLong((refreshTokenExpiredTime)) * 1000).build();
    }

    public static ResponseCookie removeRefreshTokenCookie() {
        return ResponseCookie.from("refresh-token", null)
                .build();
    }

    public static Cookie of(ResponseCookie responseCookie) {
        Cookie cookie = new Cookie(responseCookie.getName(), responseCookie.getValue());
        cookie.setPath(responseCookie.getPath());
        cookie.setSecure(responseCookie.isSecure());
        cookie.setHttpOnly(responseCookie.isHttpOnly());
        cookie.setMaxAge((int) responseCookie.getMaxAge().getSeconds());
        return cookie;
    }
}
