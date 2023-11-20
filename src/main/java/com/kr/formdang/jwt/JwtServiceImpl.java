package com.kr.formdang.jwt;


import com.kr.formdang.crypto.KeyManager;
import com.kr.formdang.provider.common.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService{

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${token.access-expired-time}")
    private long ACCESS_EXPIRED_TIME;

    @Value("${token.refresh-expired-time}")
    private long REFRESH_EXPIRED_TIME;

    @Override
    public String generateAccessToken(String subject, String uri, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("roles", roles);
        return jwtTokenProvider.createJwtToken(KeyManager.jwtKey, claims, uri, new Date(System.currentTimeMillis() + (ACCESS_EXPIRED_TIME * 1000)));
    }

    @Override
    public String generateRefreshToken(String subject, String uri, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("roles", roles);
        return jwtTokenProvider.createJwtToken(KeyManager.jwtKey, claims, uri, new Date(System.currentTimeMillis() + (REFRESH_EXPIRED_TIME * 1000)));
    }

    @Override
    public boolean validateToken(String token) {
        return jwtTokenProvider.validateJwtToken(KeyManager.jwtKey, token);
    }

    @Override
    public Date getExpiredTime(String token) {
        return jwtTokenProvider.getExpiredTime(KeyManager.jwtKey, token);
    }

    @Override
    public List<String> getRoles(String token) {
        return jwtTokenProvider.getRoles(KeyManager.jwtKey, token);
    }

    @Override
    public String getUserId(String token) {
        return jwtTokenProvider.getSubject(KeyManager.jwtKey, token);
    }

    @Override
    public String getType(String token) {
        return jwtTokenProvider.getType(KeyManager.jwtKey, token);
    }

    @Override
    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

}
