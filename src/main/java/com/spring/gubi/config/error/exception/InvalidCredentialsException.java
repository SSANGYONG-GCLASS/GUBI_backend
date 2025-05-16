package com.spring.gubi.config.error.exception;

import com.spring.gubi.config.error.ErrorCode;

public class InvalidCredentialsException extends BusinessBaseException {

	/**
     *  아이디 또는 비밀번호가 틀렸을 경우의 예외처리
     *  기본 생성자 -> 기본 정의 예외 메시지
     *  파라미터 생성자 -> 커스텀 예외 메시지
     */
	public InvalidCredentialsException() {
		super(ErrorCode.INVALID_CREDENTIALS);
	}
	
	public InvalidCredentialsException(String message) {
        super(message, ErrorCode.INVALID_CREDENTIALS);
    }

	
}
