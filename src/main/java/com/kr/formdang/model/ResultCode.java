package com.kr.formdang.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResultCode {

    SUCCESS("0", "성공"),
    SYSTEM_ERROR("17000", "시스템 에러"),
    FAIL_GENERATE_TOKEN("17001", "토큰생성 실패"),
    FAIL_VALIDATE_TOKEN("17002", "토큰 인증 실패"),
    FAIL_ISSUE_TOKEN("17003", "토큰 생성 오류"),
    EXPIRED_JWT_TOKEN("17004", "토큰만료"),
    NOT_EXIST_AUTH_KEY ("17005", "인증키 누락"),
    NOT_ALLOWED_ACCESS("17006", "접근 불가 인증키")

    ;

    private final String code;
    private final String msg;

}
