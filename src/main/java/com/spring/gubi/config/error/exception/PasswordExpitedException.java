package com.spring.gubi.config.error.exception;

import com.spring.gubi.config.error.ErrorCode;

public class PasswordExpitedException extends BusinessBaseException {

	/**
     *  로그인 시 비밀번호 변경을 3개월 간 하지 않았을 때 예외
     *  기본 생성자 -> 기본 정의 예외 메시지
     *  파라미터 생성자 -> 커스텀 예외 메시지
     */
	public PasswordExpitedException() {
		super(ErrorCode.PASSWORD_EXPIRED);
	}
	
	public PasswordExpitedException(String message) {
        super(message, ErrorCode.PASSWORD_EXPIRED);
    }

}
