package com.spring.gubi.config.error.exception;

import com.spring.gubi.config.error.ErrorCode;

public class EmailAuthFailedException extends BusinessBaseException {

	/**
     *   회원 정보를 찾지 못했을 시 예외
     *   기본 생성자 -> 기본 정의 예외 메시지
     *   파라미터 생성자 -> 커스텀 예외 메시지
     */

    public EmailAuthFailedException() {
        super(ErrorCode.EMAIL_AUTH_FAILED);
    }

    public EmailAuthFailedException(String message) {
        super(message, ErrorCode.EMAIL_AUTH_FAILED);
    }
	
}
