package com.kr.formdang.filters.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kr.formdang.jwt.JwtService;
import com.kr.formdang.model.DefaultResponse;
import com.kr.formdang.model.GlobalCode;
import com.kr.formdang.security.jwt.detail.JwtDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtDetailService jwtDetailService;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwtToken = jwtService.parseJwt(request);
            if (jwtToken != null && jwtService.validateToken(jwtToken)) this.validateJWT(jwtToken);
            filterChain.doFilter(request, response);
        }  catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException | SignatureException e) {
            log.error("[토큰 생성 에러] ===============> ");
            this.generateFailResponseBody(response, e, GlobalCode.FAIL_GENERATE_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error("[토큰 만료 에러] ===============> ");
            this.generateFailResponseBody(response, e, GlobalCode.EXPIRED_JWT_TOKEN);
        }
    }

    private void validateJWT(String token) {
        log.debug("[JwtAuthenticationFilter] JWT 인증 검사 =================>");
        UserDetails userDetails = jwtDetailService.loadUserByUsername(jwtService.getUserId(token));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void generateFailResponseBody(HttpServletResponse response, Exception e, GlobalCode code) throws IOException {
        log.error("{}", e.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), new DefaultResponse(code));
    }

}
