package com.kr.formdang.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class DefaultResponse implements RootResponse {

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final Date time = new Date(); // 날짜
    public String resultCode = ResultCode.SUCCESS.getCode();
    public String resultMsg = ResultCode.SUCCESS.getMsg();
    public Boolean success = true;

    public DefaultResponse(ResultCode code) {
        this.resultCode = code.getCode();
        this.resultMsg = code.getMsg();
        if (!ResultCode.SUCCESS.equals(code)) this.success = false;
    }

}
