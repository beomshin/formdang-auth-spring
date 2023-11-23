package com.kr.formdang.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity health() {
        log.debug("[헬스체크 성공] ==============> ");
        return ResponseEntity.ok().build();
    }
}
