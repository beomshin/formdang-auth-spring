package com.kr.formdang.provider;

import com.kr.formdang.model.JwtIssueRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Slf4j
@Configuration
public class JwtTokenProvider {

    private static String key;
    private static long ACCESS_EXPIRED_TIME;
    private static long REFRESH_EXPIRED_TIME;

    public JwtTokenProvider(
            @Value("${token.jwt-key}") String jwt_key,
            @Value("${token.access-expired-time}") long access_expired_time,
            @Value("${token.refresh-expired-time}") long refresh_expired_time

    ) {
        key = jwt_key;
        ACCESS_EXPIRED_TIME =access_expired_time;
        REFRESH_EXPIRED_TIME = refresh_expired_time;
    }

    public static String parseJwt(String headerAuth) {
        if (headerAuth == null) return null;
        return headerAuth.startsWith("Bearer ") ? headerAuth.substring(7) : headerAuth;
    }

    public static Date getExpiredTime(String token) {
        return getClaims(token).getExpiration();
    }

    public static Claims getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(parseJwt(token)).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(parseJwt(token));
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static String generateRefreshToken(JwtIssueRequest request) {
        Claims claims = Jwts.claims();
        claims.put("id", request.getId());
        claims.put("name", request.getName());
        return Jwts.builder()
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + (REFRESH_EXPIRED_TIME * 1000)))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, key)
                .setIssuer("/issue")
                .compact();
    }

    public static String generateAccessToken(JwtIssueRequest request, String uri) {
        Claims claims = Jwts.claims();
        claims.put("id", request.getId());
        claims.put("name", request.getName());
        claims.put("profile", request.getProfile());
        return Jwts.builder()
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + (ACCESS_EXPIRED_TIME * 1000)))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, key)
                .setIssuer(uri)
                .compact();
    }
}
