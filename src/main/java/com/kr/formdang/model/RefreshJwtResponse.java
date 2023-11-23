package com.kr.formdang.model;

import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RefreshJwtResponse extends DefaultResponse { // RefreshJwtTokenDefaultResponse

    private String accessToken;
    private String expiredTime;

    public RefreshJwtResponse(String accessToken, Date expiredTime)  {
        this.accessToken = accessToken;
        this.expiredTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expiredTime);
    }
}
