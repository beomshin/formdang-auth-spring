package com.kr.formdang.jwt;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

public interface JwtService {
    String generateAccessToken(String id, String uri, List<String> roles);
    String generateRefreshToken(String id, String uri, List<String> roles);
    boolean validateToken(String token);
    Date getExpiredTime(String token);
    List<String> getRoles(String token);
    String parseJwt(HttpServletRequest request);
}
