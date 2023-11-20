package com.kr.formdang.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GlobalCode {

    SUCCESS("00000", "성공"),
    SYSTEM_ERROR("17000", "시스템 에러"),
    FAIL_GENERATE_TOKEN("17001", "토큰생성 실패"),

    NOT_EXIST_USER("17002", "존재하지 않는 사용자입니다."),
    FAIL_LOGIN_INFO("17003", "아이디 또는 비밀번호가 틀립니다."),
    EXPIRED_JWT_TOKEN("17008", "토큰만료"),

    ;

    private final String code;
    private final String msg;

}
