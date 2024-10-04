package com.kr.formdang.filters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.kr.formdang.wrapper.RequestBodyWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Enumeration;

@Slf4j
//@Order(Ordered.HIGHEST_PRECEDENCE + 1)
//@WebFilter(urlPatterns = "/*")
public class DefaultHttpLoggingFilter extends OncePerRequestFilter {

  /**
   * HTTP 로깅 필터
   *
   * @param request
   * @param response
   * @param filterChain
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    StopWatch stopWatch = new StopWatch();
    String clientIp = request.getRemoteAddr();
    String realClientIp = getRealClientIp(request);

    if (StringUtils.equals(realClientIp, clientIp)) { // IP, 메소드, URL
      log.info("Request: {} [{}] [{}]", realClientIp, request.getMethod(), request.getRequestURL());
    } else {
      log.info("Request: {} → {} [{}] [{}]", realClientIp, clientIp, request.getMethod(), request.getRequestURL());
    }

//    printHeader(request);

    stopWatch.start(); // watch start
    filterChain.doFilter(request, response); // 비지니스 로직
    stopWatch.stop(); // watch stop

    log.info("Returned status=[{}] in [{}]ms, charset=[{}]", response.getStatus(), stopWatch.getTotalTimeMillis(), response.getCharacterEncoding());
  }


  private void printHeader(HttpServletRequest request) {
    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String name = headerNames.nextElement();
      log.debug("header[{}] = {}", name, request.getHeader(name));
    }
  }


  /**
   * 웹서버를 타고 들어오는 경우 실제 클라이언트의 IP를 알아낸다.<br>
   * 웹서버 쪽에서도 설정이 되어 있어야 한다.
   *
   * @param request {@link HttpServletRequest}
   * @return 실제 클라이언트 아이피
   */
  private String getRealClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }


}
