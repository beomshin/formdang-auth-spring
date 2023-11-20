package com.kr.formdang.config;

import com.kr.formdang.model.GlobalCode;
import lombok.Getter;

@Getter
public class CustomException extends Exception {

	private final GlobalCode code;

	public CustomException(GlobalCode code) {
		super(code.getMsg());
		this.code = code;
	}

}
