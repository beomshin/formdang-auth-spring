package com.kr.formdang.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GlobalCode {

    SUCCESS("00000", "성공"),
    SYSTEM_ERROR("17000", "시스템 에러"),
    FAIL_ISSUE_TOKEN("17003", "토큰 생성 오류"),
    FAIL_VALIDATE_TOKEN("17002", "토큰 인증 실패"),
    FAIL_GENERATE_TOKEN("17001", "토큰생성 실패"),
    EXPIRED_JWT_TOKEN("17008", "토큰만료"),

    ;

    private final String code;
    private final String msg;

}
