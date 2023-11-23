package com.kr.formdang.provider;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtTokenProvider {

    public String createJwtToken(String key, Claims claims, String uri, Date expiration) {
        log.debug("{}", key);
        return Jwts.builder()
                .addClaims(claims)
                .setExpiration(expiration)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, key)
                .setIssuer(uri)
                .compact();
    }

    public boolean  validateJwtToken(String key, String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("[토큰 인증 실패 에러] ==================> ");
            return false;
        }
    }

    public Date getExpiredTime(String key, String accessToken) {
        return this.getClaims(key, accessToken).getExpiration();
    }

    public String getSubject(String key, String token) {
        return this.getClaims(key, token).getSubject();
    }

    public String getType(String key, String token) {
        return this.getClaims(key,token).get("type").toString();
    }

    public List<String> getRoles(String key, String token) {
        return (List<String>) this.getClaims(key, token).get("roles");
    }

    private Claims getClaims(String key, String token) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
