package com.kr.formdang.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JwtIssueRequest {

    private String auth_key;
    private String id;
    private String name;
    private String profile;

}
