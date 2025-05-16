package com.spring.gubi.config.error.exception;

import com.spring.gubi.config.error.ErrorCode;

public class TokenExpiredException extends BusinessBaseException {

	/**
     *  토큰은 있으나 권한이 부족할 때 예외
     *  기본 생성자 -> 기본 정의 예외 메시지
     *  파라미터 생성자 -> 커스텀 예외 메시지
     */
	public TokenExpiredException() {
		super(ErrorCode.ACCESS_DENIED);
	}
	
	public TokenExpiredException(String message) {
        super(message, ErrorCode.ACCESS_DENIED);
    }

}
