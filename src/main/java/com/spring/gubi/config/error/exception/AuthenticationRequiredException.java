package com.spring.gubi.config.error.exception;

import com.spring.gubi.config.error.ErrorCode;

public class AuthenticationRequiredException extends BusinessBaseException {

	/**
     *  토큰 인증이 필요할 때 예외
     *  기본 생성자 -> 기본 정의 예외 메시지
     *  파라미터 생성자 -> 커스텀 예외 메시지
     */
	public AuthenticationRequiredException() {
		super(ErrorCode.AUTHENTICATION_REQUIRED);
	}
	
	public AuthenticationRequiredException(String message) {
        super(message, ErrorCode.AUTHENTICATION_REQUIRED);
    }

}
