package com.kr.formdang.config;

import com.kr.formdang.model.ResultCode;
import lombok.Getter;

@Getter
public class FormException extends Exception {

	private final ResultCode code;

	public FormException(ResultCode code) {
		super(code.getMsg());
		this.code = code;
	}

}
