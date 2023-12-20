package com.kr.formdang.jwt;

import com.kr.formdang.model.JwtIssueRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

public interface JwtService {
    String generateAccessToken(JwtIssueRequest request, String uri);
    String generateRefreshToken(JwtIssueRequest request, String uri);
    boolean validateToken(String token);
    Date getExpiredTime(String token);
    List<String> getRoles(String token);
    String parseJwt(HttpServletRequest request);
}
