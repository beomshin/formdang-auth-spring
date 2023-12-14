package com.kr.formdang.jwt;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

public interface JwtService {
    String generateAccessToken(String id, String uri);
    String generateRefreshToken(String id, String uri);
    boolean validateToken(String token);
    Date getExpiredTime(String token);
    List<String> getRoles(String token);
    String parseJwt(HttpServletRequest request);
}
